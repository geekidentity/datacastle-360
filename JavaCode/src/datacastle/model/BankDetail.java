package datacastle.model;

/**
 * 银行流水记录 bankDetail : 用户id,时间戳,交易类型,交易金额,工资收入标记
 * @author geekidentity
 *
 */
public class BankDetail implements Comparable<BankDetail> {
	public String userID;
	public long time;
	public int commerceType;
	public float commerceSum;
	public int salaryLabel;

	public BankDetail(String id, long ti, int comType, float comSum, int slabel) {
		userID = id;
		time = ti;
		// time = ti == 0 ? ti : (ti - 5800000000L)/86400;
		commerceType = comType;
		commerceSum = comSum;
		salaryLabel = slabel;
	}

	public int compareTo(BankDetail bankDetail) {
		return (int) (this.time - bankDetail.time);
	}

}
