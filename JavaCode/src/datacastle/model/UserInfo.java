package datacastle.model;

/**
 * 用户的基本属性 userInfo: 用户id,性别,职业,教育程度,婚姻状态,户口类型
 * @author geekidentity
 *
 */
public class UserInfo {
	public String userID;
	public String sex;
	public String job;
	public String education;
	public String marriage;
	public String family;

	public UserInfo(String userID, String sex, String job, String education, String marriage, String family) {
		this.userID = userID;
		this.sex = sex;
		this.job = job;
		this.education = education;
		this.marriage = marriage;
		this.family = family;
	}

	public String toString() {
		return userID + "," + sex + "," + job + "," + education + "," + marriage + "," + family + ",";
	}

	public static String combineFeatureTitle() {
		return "userID" + "," + "sex" + "," + "job" + "," + "education" + "," + "marriage" + "," + "family" + ",";
	}
}
