package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.InputStream;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;



public class WekaActivity extends AppCompatActivity {

    Button btnLoadModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weka);

        btnLoadModel = (Button) findViewById(R.id.btn_loadModel);

//        final FileInputStream datais;

        btnLoadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        try {
//            String arffPath = "E:/Master/DataSet/TEST/test.arff";
            DataSource source = new DataSource(getAssets().open("test.arff"));
            Instances dataset = source.getDataSet();

            dataset.setClassIndex(dataset.numAttributes()-1);
            int numClasses = dataset.numClasses();
            for (int i=0;i<numClasses;i++){
                String classValue = dataset.classAttribute().value(i);
                System.out.println(" \n the "+i+"th class value:"+classValue);
            }

            int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
            int testSize = dataset.numInstances() - trainSize;

            Instances traindataset = new Instances(dataset, 0, trainSize);
            Instances testdataset = new Instances(dataset, trainSize, testSize);

            // decission tree classifier
//            J48 tree = new J48();
//            tree.buildClassifier(traindataset);


            Toast.makeText(WekaActivity.this, "from button", Toast.LENGTH_SHORT).show();

            // -0.086055,-0.361837,-0.64796,0.057196,0.075941,0.129704,-282.665838,264.22574,147.078061,walking --> sitting
            // 0.004441,-0.006493,0.998635,-0.00011,0.000041,0.000714,-241.053011,195.015964,26.104802,sitting --> sitting
            //0.358835,0.864053,-0.005573,-0.1986,-0.122358,-0.045218,-0.1986,-0.122358,-0.045218,running --> standding

            //{walking,sitting,standing,upstairs,downstairs,running}
/*            String classValue[] = new String[]{
                    "walking", "sitting", "standing", "upstairs", "downstairs", "running"
            };

            int NUMBER_OF_ATTR = 10;
            int NUMBER_OF_INSTANCES = 1;
            final Attribute att1 = new Attribute("acc_x");
            final Attribute att2 = new Attribute("acc_y");
            final Attribute att3 = new Attribute("acc_z");
            final Attribute att4 = new Attribute("gyro_x");
            final Attribute att5 = new Attribute("gyro_y");
            final Attribute att6 = new Attribute("gyro_z");
            final Attribute att7 = new Attribute("magn_x");
            final Attribute att8 = new Attribute("magn_y");
            final Attribute att9 = new Attribute("magn_z");
            final Attribute attClass = new Attribute("class");

            FastVector fvWekaAtt = new FastVector(NUMBER_OF_ATTR);
            fvWekaAtt.addElement(att1);
            fvWekaAtt.addElement(att2);
            fvWekaAtt.addElement(att3);
            fvWekaAtt.addElement(att4);
            fvWekaAtt.addElement(att5);
            fvWekaAtt.addElement(att6);
            fvWekaAtt.addElement(att7);
            fvWekaAtt.addElement(att8);
            fvWekaAtt.addElement(att9);
            fvWekaAtt.addElement(attClass);

            Instances trainingSet = new Instances("rol", fvWekaAtt, NUMBER_OF_INSTANCES);
            trainingSet.setClassIndex(9);
            // -0.086055,-0.361837,-0.64796,0.057196,0.075941,0.129704,-282.665838,264.22574,147.078061,walking --> sitting
            // 0.004441,-0.006493,0.998635,-0.00011,0.000041,0.000714,-241.053011,195.015964,26.104802,sitting --> sitting
            //0.358835,0.864053,-0.005573,-0.1986,-0.122358,-0.045218,-0.1986,-0.122358,-0.045218,running --> standding
            Instance inst = new Instance(10);
            inst.setValue(att1, 0.358835);
            inst.setValue(att2, 0.864053);
            inst.setValue(att3, -0.005573);
            inst.setValue(att4, -0.1986);
            inst.setValue(att5, -0.122358);
            inst.setValue(att6, -0.045218);
            inst.setValue(att7, -0.1986);
            inst.setValue(att8, -0.122358);
            inst.setValue(att9, -0.045218);

            trainingSet.add(inst);*/

            addNewInstance(testdataset, -0.086055,-0.361837,-0.64796,0.057196,0.075941,0.129704,-282.665838,264.22574,147.078061);
            Toast.makeText(WekaActivity.this, "last inst" + testdataset.lastInstance(), Toast.LENGTH_LONG).show();



//            MultilayerPerceptron ann = new MultilayerPerceptron();
//            ann.buildClassifier(traindataset);
//            NaiveBayes nb = new NaiveBayes();
//            nb.buildClassifier(dataset);
//            // prediction using J48 classifier
////            J48 tree = new J48();
////            tree.buildClassifier(traindataset);
//            Instances labeled2 = new Instances(testdataset);
//            double value = ann.classifyInstance(testdataset.lastInstance());
//            labeled2.lastInstance().setClassValue(value);
//            Log.e("prediction J48 cls" , "value : " + value);
//            Log.e("prediction J48 value" , "value : " + labeled2.lastInstance().stringValue(9));

            // prediction using model
//            Classifier cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("J48model.model"));
            Classifier cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("NNSmodel.model"));
//            Classifier cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("NBmodel.model"));
//            Classifier cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("model.model"));


            Instances labeled = new Instances(testdataset);
            double preValue = cls.classifyInstance(testdataset.lastInstance());
            Log.e("prediction model value" , "value : " + labeled.lastInstance().stringValue(9));
            labeled.lastInstance().setClassValue(preValue);
            Log.e("prediction from model" , "value : " + preValue);
            Log.e("prediction model value" , "value : " + labeled.lastInstance().stringValue(9));

            // prediction using model
//            Classifier cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("J48model.model"));

//            Toast.makeText(WekaActivity.this, "last inst " + trainingSet.lastInstance(), Toast.LENGTH_LONG).show();
//            Toast.makeText(WekaActivity.this, "first inst " + trainingSet.firstInstance(), Toast.LENGTH_LONG).show();
//            Toast.makeText(WekaActivity.this, "index 0 : " + trainingSet.instance(0), Toast.LENGTH_LONG).show();

//            double prediction = cls.classifyInstance(trainingSet.firstInstance());
//            Log.e("prediction value : " , "value : " + prediction);
//            Log.e("prediction class : " , "value : " + classValue[(int)prediction]);


          /*  FileInputStream datais = new FileInputStream(datasetPath);
            InputStream dataIns = datais;
            DataSource source = new DataSource(dataIns);
            Instances dataset = source.getDataSet();
            dataset.setClassIndex(dataset.numAttributes()-1);

            // divide dataset to train dataset 80% and test dataset 20%
            int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
            int testSize = dataset.numInstances() - trainSize;


            Instances traindataset = new Instances(dataset, 0, trainSize);
            final Instances testdataset = new Instances(dataset, trainSize, testSize);

            Toast.makeText(WekaActivity.this, "from button 2", Toast.LENGTH_SHORT).show();

            // add a new instance to which we apply a prediction
            addNewInstance(testdataset, 0.358835,0.864053,-0.005573,-0.1986,-0.122358,-0.045218,-0.1986,-0.122358,-0.045218);

            Toast.makeText(WekaActivity.this, "from button 3", Toast.LENGTH_SHORT).show();

                    System.out.println("last instn testdataset : \r\n " + testdataset.lastInstance());
                    Log.e("last one","value " + testdataset.lastInstance());
            Toast.makeText(WekaActivity.this, "new instance ? " + testdataset.lastInstance(), Toast.LENGTH_SHORT).show();
*/

            // read the model ----------------------------------------------------------------------
//            mClassifier = (Classifier) weka.core.SerializationHelper.read(assetManager.open(modelPath));


        } catch (Exception e) {
            e.printStackTrace();
        }

            }
        });


    }

    // methode to add new instance to the testDataSet ----------------------------------------------
/*    public static void addNewInstance(Instances trainingSet, double acc_x, double acc_y, double acc_z,
                                      double gyro_x, double gyro_y, double gyro_z,
                                      double magn_x, double magn_y, double magn_z ){
        int NUMBER_OF_ATTR = 10;

        Attribute att1 = new Attribute("acc_x");
        Attribute att2 = new Attribute("acc_y");
        Attribute att3 = new Attribute("acc_z");
        Attribute att4 = new Attribute("gyro_x");
        Attribute att5 = new Attribute("gyro_y");
        Attribute att6 = new Attribute("gyro_z");
        Attribute att7 = new Attribute("magn_x");
        Attribute att8 = new Attribute("magn_y");
        Attribute att9 = new Attribute("magn_z");
        Attribute attClass = new Attribute("class");

        FastVector fvWekaAtt = new FastVector(NUMBER_OF_ATTR);
        fvWekaAtt.addElement(att1);
        fvWekaAtt.addElement(att2);
        fvWekaAtt.addElement(att3);
        fvWekaAtt.addElement(att4);
        fvWekaAtt.addElement(att5);
        fvWekaAtt.addElement(att6);
        fvWekaAtt.addElement(att7);
        fvWekaAtt.addElement(att8);
        fvWekaAtt.addElement(att9);
        fvWekaAtt.addElement(attClass);

        Instance inst = new Instance(10);
        inst.setValue(att1, 0.358835);
        inst.setValue(att2, 0.864053);
        inst.setValue(att3, -0.005573);
        inst.setValue(att4, -0.1986);
        inst.setValue(att5, -0.122358);
        inst.setValue(att6, -0.045218);
        inst.setValue(att7, -0.1986);
        inst.setValue(att8, -0.122358);
        inst.setValue(att9, -0.045218);

        // add
        trainingSet.add(inst);
    }*/
    public static void addNewInstance(Instances testdataset, double acc_x, double acc_y, double acc_z,
                                      double gyro_x, double gyro_y, double gyro_z,
                                      double magn_x, double magn_y, double magn_z ){

        Instance inst = new Instance(10);
        inst.setValue(testdataset.attribute(0), acc_x);
        inst.setValue(testdataset.attribute(1), acc_y);
        inst.setValue(testdataset.attribute(2), acc_z);
        inst.setValue(testdataset.attribute(3), gyro_x);
        inst.setValue(testdataset.attribute(4), gyro_y);
        inst.setValue(testdataset.attribute(5), gyro_z);
        inst.setValue(testdataset.attribute(6), magn_x);
        inst.setValue(testdataset.attribute(7), magn_y);
        inst.setValue(testdataset.attribute(8), magn_z);

        // add
        testdataset.add(inst);
    }
}
