package com.example.cyc;

public class ChatMsgEntity {
    private static final String TAG = ChatMsgEntity.class.getSimpleName();

    private String name;

    private String date;

    private String text;
    
    private String time;

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private boolean isComMeg = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }

    public String toString(){
        if (isComMeg){
            return "NAME@#$="+name+"DATE@#$="+date+"TEXT@#$="+text+"COME@#$=FFF";
        }else {
            return "NAME@#$="+name+"DATE@#$="+date+"TEXT@#$="+text+"COME@#$=000";
        }
    }

    public static ChatMsgEntity parseString(String s){
        String p1,p2,p3,p4;
        p1=s.substring("NAME@#$=".length(),s.indexOf("DATE@#$="));
        p2=s.substring(s.indexOf("DATE@#$=")+"DATE@#$=".length(),s.indexOf("TEXT@#$="));
        p3=s.substring(s.indexOf("TEXT@#$=")+"TEXT@#$=".length(),s.indexOf("COME@#$="));
        p4=s.substring(s.indexOf("COME@#$=")+"COME@#$=".length());
        boolean c=p4.indexOf("FFF")==-1?false:true;
        return new ChatMsgEntity(p1,p2,p3,c);
    }
}
