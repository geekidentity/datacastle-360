package datacastle.model;

/**
 * 用户浏览行为 browseHistory : 用户id,时间戳,浏览行为数据,浏览子行为编号
 * @author geekidentity
 *
 */
public class BrowseHistory implements Comparable<BrowseHistory> {
	public String userID;
	public long time;
	public int browseMain;
	public int browseChild;

	public BrowseHistory(String id, long ti, int bMain, int bChild) {
			userID = id;
			time = ti;
			browseMain = bMain;
			browseChild = bChild;
		}

	public int compareTo(BrowseHistory bdt) {
		return (int) (this.time - bdt.time);
	}

	public String toString() {
		String result = userID + "," + time + "," + browseMain + "," + browseChild;
		return result;
	}
}
