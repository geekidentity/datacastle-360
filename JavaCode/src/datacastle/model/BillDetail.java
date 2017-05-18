package datacastle.model;

/**
 * 信用卡账单记录bill_detail ：
 * 用户id,账单时间戳,银行id,上期账单金额,上期还款金额,信用卡额度,本期账单余额,本期账单最低还款额,消费笔数,本期账单金额,调整金额,循环利息,可用金额,预借现金额度,还款状态
 * @author geekidentity
 *
 */
public class BillDetail implements Comparable<BillDetail> {
	public String userID; // 用户id
	public long time; // 账单时间戳
	public int bankID; // 银行id
	public float lastBill; // 上期账单金额
	public float lastReturn; // 上期还款金额
	public float credit; // 信用卡额度
	public float thisBillRest; // 本期账单余额
	public float curLeastReturn; // 本期账单最低还款额
	public int consume; // 消费笔数
	public float thisBill; // 本期账单金额
	public float adjustMoney; // 调整金额
	public float cycleInterest; // 循环利息
	public float useableMoney; // 可用金额
	public float borrowMoney; // 预借现金额度
	public int returnState; // 还款状态

	public BillDetail(String id, long ti, int bid, float preBill, float prereturn, float cre, float balance,
			float lreturn, int conTimes, float curBill, float adjMoney, float cycleInt, float useMoney, float borrow,
			int state) {
		userID = id;

		if (ti == 0)
			time = ti;
		else
			time = (ti - 5800000000L) / 86400;
		// time = ti - 5800000000L;

		bankID = bid;
		lastBill = preBill;
		lastReturn = prereturn;
		credit = cre;

		thisBillRest = balance;
		curLeastReturn = lreturn;
		consume = conTimes;
		thisBill = curBill;
		adjustMoney = adjMoney;
		cycleInterest = cycleInt;
		useableMoney = useMoney;
		borrowMoney = borrow;
		returnState = state;
	}

	public String toString() {
		String billString = userID + "	" + time + "	" + bankID + "	" + lastBill + "	" + lastReturn + "	"
				+ credit + "	" + thisBillRest + "	" + curLeastReturn + "	" + consume + "	" + thisBill + "	"
				+ adjustMoney + "	" + cycleInterest + "	" + useableMoney + "	" + borrowMoney + "	" + returnState;
		return billString;
	}

	public int compareTo(BillDetail billDetail) {
		return (int) (this.time - billDetail.time);
	}

}
