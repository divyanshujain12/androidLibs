package com.androidlib.Utils;

import android.graphics.Color;
import com.androidlib.Models.BottleInfoModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by divyanshuPC on 11/15/2017.
 */
public class GraphUtils {

    private String BOTTLE_INFO = "BottleInformations";
    private String GRAPH = "Graph";
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ArrayList<BarEntry> valueSet2 = new ArrayList<>();
    ArrayList<String> xAxis = new ArrayList<>();

    private static GraphUtils ourInstance = new GraphUtils();

    public static GraphUtils getInstance() {
        return ourInstance;
    }

    private GraphUtils() {
    }


    public HashMap<String, ArrayList<BottleInfoModel>> parseBottleData(JSONObject response) {
        HashMap<String, ArrayList<BottleInfoModel>> bottleInfoHashMap = new HashMap<>();
        JSONObject bottlerInfoObject = null;
        try {
            bottlerInfoObject = response.getJSONObject(GRAPH).getJSONObject(BOTTLE_INFO);
            for (Iterator iterator = bottlerInfoObject.keys(); iterator.hasNext(); ) {
                String key = iterator.next().toString();
                ArrayList<BottleInfoModel> bottleInfoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(bottlerInfoObject.getJSONArray(key), BottleInfoModel.class);

                bottleInfoHashMap.put(key, bottleInfoModels);
            }

            return bottleInfoHashMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BarData getBarData(ArrayList<BottleInfoModel> bottleInfoModels) {
        valueSet1 = new ArrayList<>();
        valueSet2 = new ArrayList<>();
        xAxis = new ArrayList<>();
        for (int i = 0; i < bottleInfoModels.size(); i++) {
            BottleInfoModel bottleInfoModel = bottleInfoModels.get(i);
            createDataSet(i, bottleInfoModel.getLeftAmount(), bottleInfoModel.getRightAmount());
            String time = Utils.getInstance().getTimeFromTFormatToGivenFormat(bottleInfoModel.getCreatedAt(), Utils.TIME_FORMAT);
            createXAxisValues(time);
        }
        BarData data = new BarData(xAxis, getDataSet());

        return data;
    }

    private void createDataSet(int pos, int leftQuantity, int rightQuantity) {

        BarEntry v1e1 = new BarEntry(leftQuantity, pos); // Jan
        valueSet1.add(v1e1);

        BarEntry v2e1 = new BarEntry(rightQuantity, pos); // Jan
        valueSet2.add(v2e1);
    }


    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "left");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Right");
        barDataSet2.setColor(Color.rgb(155, 0, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private void createXAxisValues(String time) {
        xAxis.add(time);
    }

    public ArrayList<String> getXAxisData() {
        return xAxis;
    }

    public ArrayList<BarEntry> getValueSet1() {
        return valueSet1;
    }

    public ArrayList<BarEntry> getValueSet2() {
        return valueSet2;
    }
}
