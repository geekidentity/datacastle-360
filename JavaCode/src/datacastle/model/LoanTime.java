package datacastle.model;

/**
 * 放款时间信息loan_time : 用户id,放款时间
 * @author geekidentity
 *
 */
public class LoanTime {
	public String userID;
	public long time;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
