package datacastle.model;

/**
 * 逾期行为的记录类 overdue : 用户id,样本标签
 * @author geekidentity
 *
 */
public class Overdue {
	public String userID;
	public int label;
	public Overdue(String userID, int label) {
		this.userID = userID;
		this.label = label;
	}
}
