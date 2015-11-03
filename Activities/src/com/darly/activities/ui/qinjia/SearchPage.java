package com.darly.activities.ui.qinjia;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.darly.activities.adapter.SearchAdapter;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.ToastApp;
import com.darly.activities.ui.MainActivity;
import com.darly.activities.ui.R;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMedia;
import com.gotye.api.GotyeUser;
import com.gotye.api.GotyeUserGender;

/**
 * @author Zhangyuhui SearchPage $ 下午4:24:56 TODO 用户搜索页面
 */
public class SearchPage extends Activity {
	private ListView listview;
	private EditText input;
	private int pageIndex = 0;
	private String keyword;
	private SearchAdapter adapter;
	private int searchType = 0;
	private TextView title;

	private View loadMore, loadingView;

	private boolean loading = false;
	private boolean hasMore = true;
	public GotyeAPI api = GotyeAPI.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_search);
		loadMore = getLayoutInflater().inflate(R.layout.footview_load_more,
				null);
		loadingView = getLayoutInflater().inflate(R.layout.foot_view, null);
		listview = (ListView) findViewById(R.id.listview);
		input = (EditText) findViewById(R.id.key_word_input);
		searchType = getIntent().getIntExtra("search_type", 0);
		keyword = getIntent().getStringExtra("keyword");
		title = (TextView) findViewById(R.id.title);
		if (!TextUtils.isEmpty(keyword)) {
			input.setText(keyword);
			input.setSelection(keyword.length());
		}
		api.addListener(mDelegate);
		if (searchType == 0) {
			title.setText("搜索-好友");
		} else {
			title.setText("搜索-群");
		}

		input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		input.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH || arg1 == 0) {
					if (adapter != null) {
						adapter.clear();
					}
					keyword = input.getText().toString();
					pageIndex = 0;

					loading = false;
					hasMore = true;
					loadData();
					// listview.addFooterView(loadingView);
					return true;
				}
				return false;
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg3 == -1) {
					if (loading || !hasMore) {
						return;
					} else {
						pageIndex++;
						listview.removeFooterView(loadMore);
						listview.addFooterView(loadingView);
						loadData();
					}
					return;
				}

				if (searchType == 0) {
					GotyeUser user = (GotyeUser) adapter.getItem(arg2);
					Intent i = new Intent(SearchPage.this, UserInfoPage.class);
					i.putExtra("user", user);
					i.putExtra("from", 1);
					startActivity(i);
				} else {
					GotyeGroup group = (GotyeGroup) adapter.getItem(arg2);
					Intent i = new Intent(SearchPage.this, MainActivity.class);
					i.putExtra("group", group);
					i.putExtra("intcode", 100);
					startActivity(i);
				}

			}
		});
	}

	public void back(View v) {
		finish();

	}

	public void loadData() {
		if (loading) {
			return;
		}
		loading = true;
		if (searchType == 0) {
			api.reqSearchUserList(pageIndex, keyword, "",
					GotyeUserGender.SEX_IGNORE);
		} else {
			api.reqSearchGroup(keyword, pageIndex);
		}
	}

	@Override
	protected void onDestroy() {
		api.removeListener(mDelegate);
		super.onDestroy();
	}

	private GotyeDelegate mDelegate = new GotyeDelegate() {

		@Override
		public void onSearchUserList(int code, int pagerIndex,
				List<GotyeUser> curPageList, List<GotyeUser> mList) {
			// if (mList != null&&mList.size()>0) {
			// List<GotyeUser> tempList = new ArrayList<GotyeUser>();
			// for(GotyeUser user: mList){
			// if(tempList.contains(user)){
			// continue;
			// }else{
			// tempList.add(user);
			// }
			// }
			// if (adapter == null) {
			// adapter = new SearchAdapter(getBaseContext(), mList);
			// listview.setAdapter(adapter);
			// } else {
			// adapter.clear();
			// adapter.addFriends(mList);
			// }
			// }else{
			// hasMore=false;
			// }
			// isLoad=false;
			//
			// if(listview.getFooterViewsCount()>0){
			// listview.removeFooterView(footView);
			// }
			//
			//
			//
			//
			if (mList == null || mList.size() == 0) {
				ToastApp.showToast(SearchPage.this, R.string.search_nodata);
				hasMore = false;
			} else {
				if (curPageList == null || curPageList.size() == 0
						|| curPageList.size() < 16) {
					ToastApp.showToast(SearchPage.this,
							R.string.search_nomoredata);
					hasMore = false;
					listview.removeFooterView(loadingView);
				} else {
					hasMore = true;
					listview.removeFooterView(loadingView);
					listview.addFooterView(loadMore);
				}
			}
			if (curPageList != null && curPageList.size() > 0) {
				if (adapter == null) {
					adapter = new SearchAdapter(SearchPage.this, curPageList);
					listview.setAdapter(adapter);
				} else {
					adapter.addFriends(curPageList);
				}
			}
			loading = false;

		}

		// @Override
		// public void onGetGroupList(int code, List<GotyeGroup> grouplist) {
		// if (grouplist != null) {
		// if (adapter == null) {
		// adapter = new SearchAdapter(grouplist, SearchPage.this);
		// listview.setAdapter(adapter);
		// } else {
		// adapter.clear();
		// adapter.addGroups(grouplist);
		// }
		// }
		// }

		@Override
		public void onSearchGroupList(int code, int pageIndex,
				List<GotyeGroup> curList, List<GotyeGroup> mList) {

			if (mList == null || mList.size() == 0) {
				ToastApp.showToast(SearchPage.this, R.string.search_nodata);
				hasMore = false;
			} else {
				if (curList == null || curList.size() == 0
						|| curList.size() < 16) {
					ToastApp.showToast(SearchPage.this,
							R.string.search_nomoredata);
					hasMore = false;
					int count = listview.getFooterViewsCount();
					LogFileHelper.getInstance().i(count + "");
					listview.removeFooterView(loadingView);
				} else {
					hasMore = true;
					listview.removeFooterView(loadingView);
					listview.addFooterView(loadMore);
				}
			}
			if (curList != null && curList.size() > 0) {
				if (adapter == null) {
					adapter = new SearchAdapter(curList, SearchPage.this);
					listview.setAdapter(adapter);
				} else {
					adapter.addGroups(curList);
				}
			}
			loading = false;

		}

		@Override
		public void onDownloadMedia(int code, GotyeMedia media) {
			// TODO Auto-generated method stub
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
	};
}
