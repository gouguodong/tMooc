package com.example.tmooc;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.tmooc.adapter.AnswerAdapter;
import com.example.tmooc.entity.Answer;
import com.example.tmooc.entity.Problem;
import com.example.tmooc.utils.HttpUtils;
import com.example.tmooc.utils.MyConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProblemInfoActivity extends Activity {

	private EditText et_answer;
	private Problem problem;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				et_answer.setText("");
				Toast.makeText(getApplicationContext(),"回答成功",Toast.LENGTH_SHORT).show();
				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problem_info);
		Intent intent = getIntent();
		problem = (Problem) intent.getExtras().get("problem");
		TextView tv_problem = (TextView) findViewById(R.id.tv_problem);
		tv_problem.setText(problem.getProblem_content());
		String path = MyConstants.URL + "findAnswerByProblemId";
		new MyTask().execute(path, problem.getProblem_id() + "");
		et_answer = (EditText) findViewById(R.id.et_answer);
		Button bt_submit = (Button) findViewById(R.id.bt_submit);
		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						Map<String, Object> maps = new HashMap<String, Object>();
						maps.put("problem_id", problem.getProblem_id());
						SharedPreferences idPreferences = getSharedPreferences(
								"userId", MODE_PRIVATE);
						final int user_id = idPreferences.getInt(
								"userId_Message", 0);
						maps.put("user_id", user_id);
						maps.put("answer_content", et_answer.getText()
								.toString());
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date date = new Date(System.currentTimeMillis());
						String answer_time = formatter.format(date);
						maps.put("answer_time", answer_time);
						HttpUtils.sendPostMethod(MyConstants.URL + "addAnswer",
								maps, "utf-8");
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);

					}
				}).start();
			}
		});

	}

	public class MyTask extends AsyncTask<String, Void, List<Answer>> {
		/**
		 * 执行之前调用此方法
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		/**
		 * 后台执行操作
		 * 
		 * @return
		 */

		@Override
		protected List<Answer> doInBackground(String... params) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("problem_id", params[1]);
			String strJson = HttpUtils.sendPostMethod(params[0], map, "utf-8");
			Log.i("aaa", strJson);
			Type type = new TypeToken<List<Answer>>() {
			}.getType();
			Gson gson = new Gson();
			List<Answer> answer = gson.fromJson(strJson, type);
			return answer;
		}

		/**
		 * 执行完操作后
		 */

		@Override
		protected void onPostExecute(final List<Answer> answer) {
			// TODO Auto-generated method stub
			super.onPostExecute(answer);
			ListView lv_answer = (ListView) findViewById(R.id.lv_answer);
			AnswerAdapter adapter = new AnswerAdapter(getApplication(),
					R.layout.answer_item, answer);
			lv_answer.setAdapter(adapter);

		}
	}

}
