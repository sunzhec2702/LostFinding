package com.example.cyc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darren.lostfinding.Gdata;
import com.example.darren.lostfinding.R;
import com.example.darren.lostfinding.net.MyClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ChatMsgViewAdapter extends BaseAdapter {

	private String Uname;
	private Gdata gd;
	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatMsgEntity> coll;

	private Context ctx;

	private LayoutInflater mInflater;
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll,String mname) {
		Uname=mname;
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}
	public void setapp(Gdata g){
		gd=g;
	}
	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMsgEntity entity = coll.get(position);
		final boolean isComMsg = entity.getMsgType();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.isComMsg = isComMsg;
			viewHolder.tvPic = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ImageView iv=(ImageView) convertView
				.findViewById(R.id.iv_userhead);
		viewHolder.tvSendTime.setText(entity.getDate());
		
		if (entity.getText().contains(".amr")) {
			viewHolder.tvContent.setText("");
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
			viewHolder.tvTime.setText(entity.getTime());
		} else {
			viewHolder.tvContent.setText(entity.getText());			
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			viewHolder.tvTime.setText("");
		}
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (entity.getText().contains(".amr")) {
					playMusic(android.os.Environment.getExternalStorageDirectory() + "/" + entity.getText());
				}
			}
		});
		if (isComMsg) {
			viewHolder.tvUserName.setText(entity.getName());
			if(!Gdata.setPicLocal(entity.getName(),viewHolder.tvPic)){
				Gdata.setPicNet(entity.getName(), viewHolder.tvPic);
			}
		} else {
			viewHolder.tvUserName.setText(Uname);
			if(!Gdata.setPicLocal(Uname,viewHolder.tvPic)){
				Gdata.setPicNet(Uname, viewHolder.tvPic);
			}
		}
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public ImageView tvPic;
		public boolean isComMsg = true;
	}


	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void stop() {

	}

}
