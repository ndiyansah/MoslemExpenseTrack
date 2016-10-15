package com.MET.salman.MoeslemExpenseTracker;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LineChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart mChart;

    DatabaseHandler db;
    ArrayList<getAllData> list,list2;
    int totalExpense;

    private OnFragmentInteractionListener mListener;

    public LineChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LineChartFragment newInstance(String param1, String param2) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

        db = new DatabaseHandler(getActivity());

        mChart = (LineChart) view.findViewById(R.id.chart);
//        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(true);
        mChart.setDescription("");
        mChart.setDrawBorders(true);

        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisRight().setDrawLabels(false);


        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        setData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        setData();
    }

    public void onAttach(){
        super.onAttach(getContext());

        setData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setData()
    {
        Calendar Cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        String FormattedDate;

        ArrayList<String> xVals = new ArrayList<String>();
        Cal.add(Calendar.MONTH, -4);

        for (int i = 0;i < 4;i++)
        {
            Cal.add(Calendar.MONTH, 1);
            FormattedDate = sdf.format(Cal.getTime());
            xVals.add(FormattedDate);
        }

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        ArrayList<Entry> valuesExp = new ArrayList<Entry>();

        for (int i = 0; i < xVals.size(); i++) {
            double val = db.countExpenses(xVals.get(i));
            valuesExp.add(new Entry((float) val, i));
        }

        LineDataSet Expd = new LineDataSet(valuesExp, "Expense ");
        Expd.setLineWidth(2.5f);
        Expd.setCircleSize(4f);

        int colorExp = Color.parseColor("#FCAE28");
        Expd.setColor(colorExp);
        Expd.setCircleColor(colorExp);
        Expd.setDrawCubic(true);
        Expd.setCubicIntensity(0.1f);
        dataSets.add(Expd);



        ArrayList<Entry> valuesInc = new ArrayList<Entry>();

        for (int i = 0; i < xVals.size(); i++) {
            double val = db.countIncomes(xVals.get(i));
            valuesInc.add(new Entry((float) val, i));
        }

        LineDataSet Incd = new LineDataSet(valuesInc, "Income ");
        Incd.setLineWidth(2.5f);
        Incd.setCircleSize(4f);

        int colorInc = Color.parseColor("#EB5244");
        Incd.setColor(colorInc);
        Incd.setCircleColor(colorInc);
        Incd.setDrawCubic(true);
        Incd.setCubicIntensity(0.1f);
        dataSets.add(Incd);


//        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
//        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }
}
