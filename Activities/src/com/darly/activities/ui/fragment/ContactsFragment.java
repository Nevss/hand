package com.darly.activities.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.darly.activities.R;
import com.darly.activities.adapter.ContactsAdapter;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.ToastApp;
import com.darly.activities.ui.ChatPage;
import com.darly.activities.ui.qinjia.SearchPage;
import com.darly.activities.ui.qinjia.SideBar;
import com.darly.activities.ui.qinjia.SideBar.OnTouchingLetterChangedListener;
import com.darly.activities.ui.qinjia.UserInfoPage;
import com.darly.activities.ui.qinjia.bean.GotyeUserProxy;
import com.darly.activities.ui.qinjia.util.CharacterParser;
import com.darly.activities.ui.qinjia.util.PinyinComparator;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeCustomerService;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeMedia;
import com.gotye.api.GotyeUser;

/**
 * @author Zhangyuhui ContactsFragment $ 下午4:25:13 TODO 用户好友展示页面。
 */
public class ContactsFragment extends Fragment implements OnClickListener {
	private static final String TAG = "ContactsFragment";
	private ListView userListView;
	private SideBar sideBar;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator = new PinyinComparator();
	private ArrayList<GotyeUserProxy> proxyFrinds = new ArrayList<GotyeUserProxy>();
	private List<GotyeUser> friends = new ArrayList<GotyeUser>();
	private ContactsAdapter adapter;
	public String currentLoginName;
	private EditText search;
	private boolean selectedFriendTab = true;
	private TextView friendTab, blockTab;
	private boolean showAddFriendTip = false;
	private GotyeCustomerService mCServer;
	public GotyeAPI api = GotyeAPI.getInstance();

	private ProgressDialogUtil loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_contacts, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// api.addListener(this);
		api.addListener(mDelegate);
		characterParser = CharacterParser.getInstance();
		GotyeUser user = api.getLoginUser();
		currentLoginName = user.getName();
		loadLocalFriends();
		api.reqFriendList();
		initView();
		setAdapter();
		int state = api.isOnline();
		if (state != 1) {
			setErrorTip(0);
		} else {
			setErrorTip(1);
		}
	}

	private void loadLocalFriends() {
		friends = api.getLocalFriendList();
		handleUser(friends);
	}

	private void loadLocalBlocks() {
		friends = api.getLocalBlockedList();
		handleUser(friends);
	}

	public void initView() {
		friendTab = (TextView) getView().findViewById(R.id.friend_tab);
		blockTab = (TextView) getView().findViewById(R.id.block_tab);

		friendTab.setOnClickListener(this);
		blockTab.setOnClickListener(this);

		sideBar = (SideBar) getView().findViewById(R.id.sidrbar);
		userListView = (ListView) getView().findViewById(R.id.listview);
		getView().findViewById(R.id.add).setOnClickListener(this);
		search = (EditText) getView().findViewById(R.id.contact_search_input);
		search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String keyword = s.toString();
				search(keyword);
			}
		});

		loading = new ProgressDialogUtil(getActivity());
		loading.setMessage(null);
	}

	private void search(String keyWord) {
		if (TextUtils.isEmpty(keyWord)) {
			selectUserByKeyword(null);
		} else {
			selectUserByKeyword(keyWord);
		}
		adapter.notifyDataSetChanged();

	}

	private void handleUser(List<GotyeUser> userList) {
		proxyFrinds.clear();
		if (userList != null) {
			for (GotyeUser user : userList) {
				String pinyin = characterParser.getSelling(user.getName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				GotyeUserProxy userProxy = new GotyeUserProxy(user);
				if (sortString.matches("[A-Z]")) {
					userProxy.firstChar = sortString.toUpperCase();
				} else {
					userProxy.firstChar = "#";
				}
				proxyFrinds.add(userProxy);
			}
			Collections.sort(proxyFrinds, pinyinComparator);
		}
		GotyeUserProxy room = new GotyeUserProxy(new GotyeUser());
		room.gotyeUser.setId(-2);
		room.firstChar = "↑";
		proxyFrinds.add(0, room);
		GotyeUserProxy group = new GotyeUserProxy(new GotyeUser());
		group.gotyeUser.setId(-1);
		group.firstChar = "↑";
		proxyFrinds.add(1, group);

		// GotyeUserProxy server = new GotyeUserProxy(new GotyeUser());
		// server.gotyeUser.setId(-3);
		// server.firstChar = "↑";
		// proxyFrinds.add(2, server);

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

	@SuppressLint("DefaultLocale")
	private void selectUserByKeyword(String keyWord) {
		proxyFrinds.clear();
		if (friends != null) {
			for (GotyeUser user : friends) {
				if (user.getId() < 0) {
					continue;
				}
				String pinyin = characterParser.getSelling(user.getName());
				if (keyWord != null) {
					if (!pinyin.startsWith(keyWord.toLowerCase())) {
						continue;
					}
				}
				String sortString = pinyin.substring(0, 1).toUpperCase();
				GotyeUserProxy userProxy = new GotyeUserProxy(user);
				if (sortString.matches("[A-Z]")) {
					userProxy.firstChar = sortString.toUpperCase();
				} else {
					userProxy.firstChar = "#";
				}
				proxyFrinds.add(userProxy);
			}
			Collections.sort(proxyFrinds, pinyinComparator);

		}
		GotyeUserProxy room = new GotyeUserProxy(new GotyeUser());
		room.gotyeUser.setId(-2);
		room.firstChar = "↑";
		proxyFrinds.add(0, room);
		GotyeUserProxy group = new GotyeUserProxy(new GotyeUser());
		group.gotyeUser.setId(-1);
		group.firstChar = "↑";
		proxyFrinds.add(1, group);
	}

	private void setAdapter() {
		adapter = new ContactsAdapter(getActivity(), proxyFrinds);
		userListView.setAdapter(adapter);
		userListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				GotyeUserProxy userProxy = ((GotyeUserProxy) adapter
						.getItem(arg2));
				if (selectedFriendTab) {
					if (userProxy.gotyeUser.getId() == -2) {
						// Intent room = new Intent(getActivity(),
						// GroupRoomListPage.class);
						// room.putExtra("type", 0);
						// startActivity(room);
						return;
					} else if (userProxy.gotyeUser.getId() == -1) {
						// Intent group = new Intent(getActivity(),
						// GroupRoomListPage.class);
						// group.putExtra("type", 1);
						// startActivity(group);
						return;
					} else if (userProxy.gotyeUser.getId() == -3) {
						showServerDialog();
						return;
					}
					Intent i = new Intent(getActivity(), ChatPage.class);
					i.putExtra("user", userProxy.gotyeUser);
					i.putExtra("from", 200);
					startActivity(i);
				} else {
					if (userProxy.gotyeUser.getId() == -2) {
						// Intent room = new Intent(getActivity(),
						// GroupRoomListPage.class);
						// room.putExtra("type", 0);
						// startActivity(room);
						return;
					} else if (userProxy.gotyeUser.getId() == -1) {
						// Intent group = new Intent(getActivity(),
						// GroupRoomListPage.class);
						// group.putExtra("type", 1);
						// startActivity(group);
						return;
					} else if (userProxy.gotyeUser.getId() == -3) {
						showServerDialog();
						return;
					}
					Intent i = new Intent(getActivity(), UserInfoPage.class);
					i.putExtra("user", userProxy.gotyeUser);
					startActivity(i);
				}
			}
		});
		userListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				GotyeUserProxy userProxy = ((GotyeUserProxy) adapter
						.getItem(arg2));
				if (userProxy.gotyeUser.getId() < 0) {
					return true;
				}
				Intent i = new Intent(getActivity(), UserInfoPage.class);
				i.putExtra("user", userProxy.gotyeUser);
				startActivity(i);
				return true;
			}
		});
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					userListView.setSelection(position);
				}

			}
		});

	}

	public void refresh() {
		if (selectedFriendTab) {
			loadLocalFriends();
		} else {
			loadLocalBlocks();
		}
	}

	private void showServerDialog() {
		final EditText input = new EditText(getActivity());
		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.contacts_serv)
				.setView(input)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if (input.getText() == null
										|| input.getText().length() == 0) {
									Toast.makeText(getActivity(),
											R.string.contacts_serv,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String gId = input.getText().toString();
								long groupId = Long.parseLong(gId);
								mCServer = new GotyeCustomerService(groupId);
								Intent cserver = new Intent(getActivity(),
										ChatPage.class);
								cserver.putExtra("cserver", mCServer);
								startActivity(cserver);
							}
						}).setNegativeButton(R.string.no, null).show();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.add:
			showTools(arg0);
			break;
		case R.id.tools_add:
			addUser();
			break;
		case R.id.tools_add_single:
			addSingelUser();
			break;
		case R.id.tools_group_chat:
			if (tools.isShowing()) {
				tools.dismiss();
				// Intent toCreateGroup = new Intent(getActivity(),
				// CreateGroupSelectUser.class);
				// startActivity(toCreateGroup);
			}
			break;
		case R.id.friend_tab:
			selectedFriendTab = true;
			loadLocalFriends();
			api.reqFriendList();
			friendTab.setTextColor(getResources().getColor(R.color.app_color));
			blockTab.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.block_tab:
			selectedFriendTab = false;
			loadLocalBlocks();
			api.reqBlockedList();
			blockTab.setTextColor(getResources().getColor(R.color.app_color));
			friendTab.setTextColor(getResources().getColor(R.color.black));
			break;
		default:
			break;
		}
	}

	private void addUser() {
		if (tools.isShowing()) {
			tools.dismiss();
			Intent toSreach = new Intent(getActivity(), SearchPage.class);
			toSreach.putExtra("search_type", 0);
			startActivity(toSreach);
		}
	}

	private void addSingelUser() {
		if (tools.isShowing()) {
			tools.dismiss();

			final EditText input = new EditText(getActivity());

			new AlertDialog.Builder(getActivity())
					.setTitle(R.string.be_friend)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(input)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String name = input.getText().toString();
									if (!TextUtils.isEmpty(name)) {
										if (name.equals(currentLoginName)) {
											ToastApp.showToast(getActivity(),
													R.string.contacts_noself);
											return;
										}
										loading.show();
										api.reqAddFriend(new GotyeUser(name));
										showAddFriendTip = true;
									}
								}
							}).setNegativeButton(R.string.no, null).show();

		}
	}

	public void hideKeyboard(View view) {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private PopupWindow tools;

	private void showTools(View v) {
		View toolsLayout = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_tools, null);
		toolsLayout.findViewById(R.id.tools_add).setOnClickListener(this);
		toolsLayout.findViewById(R.id.tools_add_single)
				.setOnClickListener(this);
		toolsLayout.findViewById(R.id.tools_group_chat)
				.setOnClickListener(this);
		tools = new PopupWindow(toolsLayout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		tools.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		tools.setOutsideTouchable(false);
		tools.showAsDropDown(v, 0, 20);
		tools.update();
	}

	@Override
	public void onDestroy() {
		api.removeListener(mDelegate);
		super.onDestroy();
	}

	private void setErrorTip(int code) {
		if (code == 1) {
			getView().findViewById(R.id.error_tip).setVisibility(View.GONE);
		} else {
			getView().findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if (code == -1) {
				getView().findViewById(R.id.loading)
						.setVisibility(View.VISIBLE);
				((TextView) getView().findViewById(R.id.showText))
						.setText(R.string.contacts_neting);
				getView().findViewById(R.id.error_tip_icon).setVisibility(
						View.GONE);
			} else {
				getView().findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView) getView().findViewById(R.id.showText))
						.setText(R.string.contacts_notneting);
				getView().findViewById(R.id.error_tip_icon).setVisibility(
						View.VISIBLE);
			}
		}
	}

	private GotyeDelegate mDelegate = new GotyeDelegate() {

		@Override
		public void onGetFriendList(int code, List<GotyeUser> mList) {
			refresh();
		}

		@Override
		public void onGetBlockedList(int code, List<GotyeUser> mList) {
			refresh();
		}

		@Override
		public void onAddFriend(int code, GotyeUser user) {
			loading.dismiss();
			if (code == 0) {
				if (showAddFriendTip) {
					ToastApp.showToast(getActivity(), R.string.contacts_addfriend_succ);
				}

				loadLocalFriends();
			} else {
				if (showAddFriendTip) {
					ToastApp.showToast(getActivity(), R.string.contacts_addfriend_fail);
				}
			}
			showAddFriendTip = false;
		}

		@Override
		public void onDownloadMedia(int code, GotyeMedia media) {
			if (getActivity().isTaskRoot()) {
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onLogin(int code, GotyeUser currentLoginUser) {
			setErrorTip(1);
		}

		@Override
		public void onLogout(int code) {
			setErrorTip(0);
		}

		@Override
		public void onReconnecting(int code, GotyeUser currentLoginUser) {
			int onlineStatus = api.isOnline();
			LogFileHelper.getInstance().i(TAG, onlineStatus + "");
			setErrorTip(-1);
		}
	};
}
