package com.example.sino.view.fragment.chatmessage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.SuccessChatReceiveBean;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.model.db.ChatMessage;
import com.example.sino.model.db.User;
import com.example.sino.model.userInfobyid.SuccessUserInfoByIdBean;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ChatMessageFragment extends Fragment {

    private User user;
    private DatabaseViewModel viewModel;
    private MainViewModel mainViewModel;
    private String inputParam = "";
    private CompositeDisposable compositeDisposable;
    private SuccessChatReceiveBean successChatReceiveBean;
    private SimpleDateFormat simpleDateFormat;
    private View view;
    private ChatGroup chatGroup;
    private boolean isPrivateChatMessage = false;
    private Long receiverUserId = null;
    private Boolean groupIsPrivate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_chat_message, container, false);
            if (getArguments() != null){
                chatGroup = getArguments().getParcelable("chatGroup");
            }
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        compositeDisposable = new CompositeDisposable();
        inputParam = GsonGenerator.getUserPermissionList(user.getUsername(), user.getBisPassword());
        simpleDateFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);

        getUserChatMessageList();

        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatMessage("");
            }
        });
    }

    private void getUserChatMessageList() {
        mainViewModel.getUserChatMessageListVM(inputParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SuccessChatReceiveBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessChatReceiveBean successChatReceive) {
                        successChatReceiveBean = successChatReceive;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (successChatReceiveBean.getRESULT() != null) {
                            if (successChatReceiveBean.getRESULT().getChatMessageReceiverList().size() > 0) {
                                List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();

                                for (int i = 0; i < successChatReceiveBean.getRESULT().getChatMessageReceiverList().size(); i++) {
                                    ChatMessage message = successChatReceiveBean.getRESULT().getChatMessageReceiverList().get(i).chatMessage;
                                    ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.setReadIs(false);
                                    chatMessage.setDeliverIs(true);
                                    chatMessage.setDeliverDate(new Date());
                                    chatMessage.setAttachFileSize(0);
                                    chatMessage.setAttachFileReceivedSize(0);

                                    if (message != null) {
                                        chatMessage.setId(message.getId());
                                        chatMessage.setSenderUserId(message.getSenderUserId());
                                        chatMessage.setValidUntilDate(message.getValidUntilDate());
                                        chatMessage.setMessage(message.getMessage());
                                        chatMessage.setSendDate(message.getSendDate());
                                        chatMessage.setDateCreation(message.getDateCreation());
                                        chatMessage.setChatGroupId(message.getChatGroupId());
                                        chatMessage.setAttachFileUserFileName(message.getAttachFileUserFileName());
                                        chatMessage.setAttachFileSize(message.getAttachFileSize());
                                        chatMessage.setSenderAppUserId(message.getSenderAppUserId());

                                        chatMessageList.add(chatMessage);
                                    }

                                }

                                List<Long> messageIdList = new ArrayList<>();
                                for (ChatMessage chatMessage : chatMessageList) {
                                    List<ChatMessage> tmpChatMessageList = viewModel.getChatMessagesByServerMessageIdVM(chatMessage.getServerMessageId());
                                    if (tmpChatMessageList.isEmpty()) {
                                        //viewModel.insertChatMessageVM(chatMessage);
                                        messageIdList.add(Long.valueOf(chatMessage.getId()));
                                    }
                                }

                                inputParam = GsonGenerator.chatMessageDeliveredReport(user.getUsername(), user.getBisPassword(), messageIdList);
                                callChatMessageDeliveredReport();
                            }
                        }
                    }
                });
    }

    private void callChatMessageDeliveredReport() {
        mainViewModel.chatMessageDeliveredReportVM(inputParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SuccessChatReceiveBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessChatReceiveBean successChatReceive) {
                        successChatReceiveBean = successChatReceive;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(successChatReceiveBean.getSUCCESS());
                        System.out.println(successChatReceiveBean.getRESULT());
                        System.out.println(successChatReceiveBean.getERROR());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void getChatMessageList(){
        ChatMessage tmpChatMessageFS = new ChatMessage();
        tmpChatMessageFS.setCreateNewPvChatGroup(false);
        Integer loadMessageCount = 80;
        tmpChatMessageFS.setChatGroupId(Integer.valueOf(String.valueOf(chatGroup.getServerGroupId())));
        tmpChatMessageFS.setCreateNewPvChatGroup(false);
    }

    private void sendChatMessage(String message_str) {
        String message = "Hello Test";
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatGroupId(Integer.valueOf(String.valueOf(chatGroup.getServerGroupId())));
        chatMessage.setDateCreation(String.valueOf(new Date()));
        chatMessage.setMessage(message);
        chatMessage.setSenderAppUserId(user.getServerUserId());

        System.out.println("getServerUserId===" + user.getServerUserId());
        System.out.println("getId===" + chatMessage.getId());

        //chatMessage.setSendingStatusEn(SendingStatusEn.Pending.ordinal());
        chatMessage.setSendingStatusDate(new Date());
        chatMessage.setDeliverIs(Boolean.FALSE);
        chatMessage.setReadIs(Boolean.FALSE);
        chatMessage.setReceiverAppUserId(user.getServerUserId());
        chatMessage.setCreateNewPvChatGroup(isPrivateChatMessage);
        chatMessage.setSendingStatusEn(SendingStatusEn.InProgress.ordinal());
        chatMessage.setSendingStatusDate(new Date());
        if (groupIsPrivate) {
            chatMessage.setCreateNewPvChatGroup(true);
        }
       Long id = viewModel.insertChatMessageVM(chatMessage);


        inputParam = GsonGenerator.saveChatMessage(user.getUsername(), user.getBisPassword(),id,viewModel);
        mainViewModel.saveChatMessageRV(inputParam).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SuccessUserInfoByIdBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessUserInfoByIdBean successUserInfoByIdBean) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}