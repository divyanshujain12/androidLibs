package com.androidlib.Fragments.chatFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.androidlib.Constants.LibAPI;
import com.androidlib.Constants.LibApiCodes;
import com.androidlib.Constants.LibConstants;
import com.androidlib.CustomFontViews.CustomButtonRegular;
import com.androidlib.CustomFontViews.CustomEditTextRegular;
import com.androidlib.GlobalClasses.BaseFragment;
import com.androidlib.Models.AllTypeUserModel;
import com.androidlib.Models.ChatModel;
import com.androidlib.Utils.CallWebService;
import com.androidlib.Utils.UniversalParser;
import com.androidlib.adapters.AllTypeUserSPAdapter;
import com.androidlib.adapters.ChatAdapter;
import com.locationlib.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public class MotherChatFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {

    AppCompatSpinner usersSP;
    RecyclerView chatRV;
    CustomEditTextRegular messageET;
    CustomButtonRegular sendMsgBT;
    LinearLayout messageLL;
    View view;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatModel> chatModels;
    private ArrayList<AllTypeUserModel> allTypeUserModels;
    private AllTypeUserSPAdapter allTypeUserSPAdapter;
    private String selectedMotherID = "";
    private static String fromType = "", id = "";
    private String content;

    public static MotherChatFragment getInstance(String fromType, String id) {
        MotherChatFragment motherChatFragment = new MotherChatFragment();
        MotherChatFragment.fromType = fromType;
        MotherChatFragment.id = id;

        return motherChatFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InitViews();
    }

    private void InitViews() {
        usersSP = (AppCompatSpinner) view.findViewById(R.id.usersSP);
        messageET = (CustomEditTextRegular) view.findViewById(R.id.messageET);
        sendMsgBT = (CustomButtonRegular) view.findViewById(R.id.sendMsgBT);
        chatRV = (RecyclerView) view.findViewById(R.id.chatRV);
        sendMsgBT.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatRV.setLayoutManager(linearLayoutManager);

        CallWebService.getInstance(getContext(), true, LibApiCodes.LIB_MOTHERS).hitJsonObjectRequestAPI(CallWebService.GET, LibAPI.MOTHERS, null, this);
        messageET.addTextChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(View v) {
        CallWebService.getInstance(getContext(), false, LibApiCodes.POST_CHAT).hitJsonObjectRequestAPI(CallWebService.POST, LibAPI.POST_MESSAGE, createJsonForPostChat(), this);
        ChatModel chatModel = new ChatModel();
        chatModel.setContent(content);
        chatModel.setSameSide(1);
        enableDisableSendBtn(false);
        messageET.setText("");

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case LibApiCodes.LIB_MOTHERS:
                allTypeUserModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray("mothers"), AllTypeUserModel.class);
                allTypeUserSPAdapter = new AllTypeUserSPAdapter(getContext(), 1, allTypeUserModels);
                usersSP.setAdapter(allTypeUserSPAdapter);
                usersSP.setOnItemSelectedListener(this);
                break;
            case LibApiCodes.GET_CHAT:
                chatModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray("Messages"), ChatModel.class);
                chatAdapter = new ChatAdapter(getContext(), chatModels, this);
                chatRV.setAdapter(chatAdapter);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMotherID = allTypeUserModels.get(position).getId();
        loadChat();
    }

    private void loadChat() {
        CallWebService.getInstance(getContext(), true, LibApiCodes.GET_CHAT).hitJsonObjectRequestAPI(CallWebService.POST, LibAPI.GET_CHAT, createJsonForGetChat(), this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private JSONObject createJsonForGetChat() {
        try {
            JSONObject toJsonObject = new JSONObject();
            toJsonObject.put(LibConstants.MOTHER_ID, selectedMotherID);

            JSONObject fromJsonObject = new JSONObject();
            fromJsonObject.put(fromType, id);

            JSONObject mainJsonObject = new JSONObject();
            mainJsonObject.put(LibConstants.TO, toJsonObject);
            mainJsonObject.put(LibConstants.FROM, fromJsonObject);

            return mainJsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject createJsonForPostChat() {
        content = messageET.getText().toString();
        JSONObject postJson = createJsonForGetChat();
        try {
            postJson.put(LibConstants.CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postJson;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            enableDisableSendBtn(true);

        else
            enableDisableSendBtn(false);


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onResume() {
        super.onResume();
        enableDisableSendBtn(false);
    }

    private void enableDisableSendBtn(boolean enable) {
        if (enable) {
            sendMsgBT.setEnabled(true);
            sendMsgBT.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            sendMsgBT.setEnabled(false);
            sendMsgBT.setTextColor(getResources().getColor(R.color.light_gray));
        }
    }
}
