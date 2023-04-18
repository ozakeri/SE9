package com.example.sino.view.fragment.chatmessage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.enumtype.GeneralStatus;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.model.chatgroup.SuccessChatGroupBean;
import com.example.sino.model.chatgroupmember.ChatGroupMember;
import com.example.sino.model.chatgroupmember.SuccessChatGroupMemberBean;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.User;
import com.example.sino.model.userInfobyid.SuccessUserInfoByIdBean;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.view.adapter.chatadapters.ChatGroupAdapter;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.rejowan.cutetoast_custom.CuteToast;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ChatGroupFragment extends Fragment {

    private MainViewModel mainViewModel;
    private String inputParam = "";
    private CircularProgressView progress_circular;
    private User user;
    private DatabaseViewModel databaseViewModel;
    private AppUser appUser;
    private boolean isUpdate;
    private RecyclerView chatGroupRV;
    private ChatGroupAdapter chatGroupAdapter;
    private View view;
    private CompositeDisposable compositeDisposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_caht_group, container, false);
            user = SinoApplication.getInstance().getCurrentUser();
            compositeDisposable = new CompositeDisposable();
            mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
            progress_circular = view.findViewById(R.id.progress_circular);
            chatGroupRV = view.findViewById(R.id.chatGroupRV);
            inputParam = GsonGenerator.getUserPermissionList(user.getUsername(), user.getBisPassword());
            getUserChatGroupList();
            getChatGroupMemberList();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getUserChatGroupList() {
        Util.showProgress(progress_circular);
        mainViewModel.getUserChatGroupList(inputParam).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessChatGroupBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessChatGroupBean successChatGroupBean) {
                        if (successChatGroupBean.success != null) {
                            if (successChatGroupBean.result != null) {
                                if (successChatGroupBean.result.chatGroupList != null) {
                                    System.out.println("chatGroupList.size====" + successChatGroupBean.result.chatGroupList.size());

                                    for (int i = 0; i < successChatGroupBean.result.chatGroupList.size(); i++) {
                                        Long serverGroupId = Long.valueOf(successChatGroupBean.result.chatGroupList.get(i).id);
                                        ChatGroup tmpChatGroupFS = new ChatGroup();
                                        tmpChatGroupFS.setServerGroupId(serverGroupId);

                                        ChatGroup chatGroup = databaseViewModel.getChatGroupByServerGroupIdVM(tmpChatGroupFS.getServerGroupId());
                                        if (chatGroup == null) {
                                            chatGroup = new ChatGroup();
                                            chatGroup.setServerGroupId(serverGroupId);
                                        }

                                        System.out.println("chatGroupList.get(i).name=====" + successChatGroupBean.result.chatGroupList.get(i).name);
                                        chatGroup.setName(successChatGroupBean.result.chatGroupList.get(i).name);
                                        chatGroup.setPrivateIs(successChatGroupBean.result.chatGroupList.get(i).getPrivateIs());
                                        chatGroup.setMaxMember(successChatGroupBean.result.chatGroupList.get(i).getMaxMember());
                                        chatGroup.setNotifyAct(successChatGroupBean.result.chatGroupList.get(i).getNotifyAct());
                                        chatGroup.setStatus(successChatGroupBean.result.chatGroupList.get(i).getStatus());

                                        if (chatGroup.getId() == null) {
                                            databaseViewModel.insertChatGroupVM(chatGroup);
                                        } else {
                                            databaseViewModel.updateChatGroupVM(chatGroup);
                                        }

                                        ChatGroup chatGroupUserRemoved = databaseViewModel.getChatGroupListByParamVM(serverGroupId);
                                        if (chatGroupUserRemoved != null) {
                                            chatGroupUserRemoved.setStatusEn(GeneralStatus.Inactive.ordinal());
                                            databaseViewModel.updateChatGroupVM(chatGroupUserRemoved);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Util.hideProgress(progress_circular);

                    }

                    @Override
                    public void onComplete() {
                        setupChatGroupRV();
                    }
                });
    }

    private void getChatGroupMemberList() {
        mainViewModel.getChatGroupMemberList(inputParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessChatGroupMemberBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessChatGroupMemberBean successChatGroupMemberBean) {
                        if (successChatGroupMemberBean.success != null) {
                            if (successChatGroupMemberBean.result != null) {

                                List<Long> serverGroupIdList = new ArrayList<Long>();

                                if (successChatGroupMemberBean.result.chatGroupMemberList != null) {
                                    for (int i = 0; i < successChatGroupMemberBean.result.chatGroupMemberList.size(); i++) {
                                        Long serverGroupId = (long) successChatGroupMemberBean.result.chatGroupMemberList.get(i).getId();
                                        serverGroupIdList.add(serverGroupId);
                                        ChatGroup tmpChatGroupFS = new ChatGroup();
                                        tmpChatGroupFS.setServerGroupId(serverGroupId);
                                        ChatGroup chatGroup = databaseViewModel.getChatGroupByServerGroupIdVM(tmpChatGroupFS.getServerGroupId());
                                        if (chatGroup == null) {
                                            chatGroup = new ChatGroup();
                                            chatGroup.setServerGroupId(serverGroupId);
                                        }
                                        chatGroup.setName(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getName());
                                        chatGroup.setMaxMember(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getMaxMember());
                                        chatGroup.setNotifyAct(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getNotifyAct());
                                        chatGroup.setStatus(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getStatus());

                                        if (chatGroup.getId() == null) {
                                            databaseViewModel.insertChatGroupVM(chatGroup);
                                        } else {
                                            databaseViewModel.updateChatGroupVM(chatGroup);
                                        }

                                        System.out.println("i=============" + i);
                                        List<Long> userIdList = new ArrayList<Long>();
                                        if (successChatGroupMemberBean.result.chatGroupMemberList.get(i).getChatGroupMembers() != null) {

                                            for (int j = 0; j < successChatGroupMemberBean.result.chatGroupMemberList.get(i).getChatGroupMembers().size(); j++) {
                                                System.out.println("j=============" + j);
                                                Long userId = Long.valueOf(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getChatGroupMembers().get(j).userId);
                                                userIdList.add(userId);
                                                ChatGroupMember tmpChatGroupMemberFS = new ChatGroupMember();
                                                tmpChatGroupMemberFS.setAppUserId(userId);
                                                if (chatGroup.getId() != null){
                                                    tmpChatGroupMemberFS.setChatGroupId(Long.valueOf(chatGroup.getId()));
                                                }

                                                ChatGroupMember chatGroupMember = databaseViewModel.getChatGroupMemberByUserAndGroupVM(userId, Long.valueOf(chatGroup.getId()));
                                                if (chatGroupMember == null) {
                                                    chatGroupMember = new ChatGroupMember();
                                                    chatGroupMember.setAppUserId(userId);
                                                    chatGroupMember.setChatGroupId(Long.valueOf(chatGroup.getId()));
                                                }
                                                chatGroupMember.setPrivilegeTypeEn(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getChatGroupMembers().get(j).privilegeTypeEn);
                                                chatGroupMember.setAdminIs(successChatGroupMemberBean.result.chatGroupMemberList.get(i).getChatGroupMembers().get(j).getAdminIs());
                                                if (chatGroupMember.getId() == null) {
                                                    databaseViewModel.insertChatGroupMemberVM(chatGroupMember);
                                                } else {
                                                    databaseViewModel.updateChatGroupMemberVM(chatGroupMember);
                                                }

                                                AppUser appUser = mainViewModel.getAppUserByIdVM(chatGroupMember.getAppUserId());
                                                if (appUser == null) {
                                                    appUser = new AppUser();

                                                    getUserById(getContext(), user, chatGroupMember.getAppUserId(), appUser, false);
                                                } else if (appUser.getName() == null || appUser.getFamily() == null) {
                                                    getUserById(getContext(), user, chatGroupMember.getAppUserId(), appUser, true);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUserById(Context context, User user, Long appUserId, AppUser appUser, boolean isUpdate) {
        this.appUser = appUser;
        this.isUpdate = isUpdate;
        inputParam = GsonGenerator.getUserInfoById(user.getUsername(), user.getBisPassword(), appUserId);
        mainViewModel.getUserInfoById(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessUserInfoByIdBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessUserInfoByIdBean successUserInfoByIdBean) {
                        if (successUserInfoByIdBean.success != null) {
                            if (successUserInfoByIdBean.result != null) {
                                if (successUserInfoByIdBean.result.user != null) {
                                    appUser.setName(successUserInfoByIdBean.result.user.name);
                                    appUser.setFamily(successUserInfoByIdBean.result.user.family);
                                    appUser.setId(successUserInfoByIdBean.result.user.id);

                                    if (isUpdate) {
                                        databaseViewModel.insertAppUserVM(appUser);
                                    } else {
                                        databaseViewModel.updateAppUserVM(appUser);
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void setupChatGroupRV() {
        Util.hideProgress(progress_circular);
        chatGroupRV.setVisibility(View.VISIBLE);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        chatGroupRV.setLayoutManager(mLayoutManager);
        List<ChatGroup> chatGroupList = databaseViewModel.getActiveChatGroupListVM(GeneralStatus.Active.ordinal());
        chatGroupAdapter = new ChatGroupAdapter(chatGroupList);
        chatGroupRV.setAdapter(chatGroupAdapter);

        chatGroupAdapter.setOnItemClickListener(new ChatGroupAdapter.onIemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Bundle bundle = new Bundle();
                ChatGroup chatGroup = chatGroupList.get(position);
                CuteToast.ct(getActivity(), chatGroup.getName(), CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, R.drawable.group_people).show();
                //bundle.putParcelable("chatGroup" , chatGroup);
                //Navigation.findNavController(view).navigate(R.id.chatMessageFragment,bundle);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}