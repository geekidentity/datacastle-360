package datacastle;

/*
 * CleanData类 作用： 清洗数据，去掉训练集和测试集中的冗余数据！
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import datacastle.model.BillDetail;

public class DataClean {
	// public static long repeatBills = 0L;
	// public static long relativeCount = 0L;
	public static HashSet<String> goodRepeat = new HashSet<String>();

	public static void main(String[] argv) throws IOException {
		String dataPath = "../data/train/";
		String testPath = "../data/test/";

		String NewdataPath = "train/LabeledTrain/";

		// 训练集：账单记录、浏览行为记录、银行流水记录去除重复数据
		// deleteRepeatRecord(dataPath+"bill_detail_train.txt",dataPath+"clean_bill_detail_train.txt");
		// deleteRepeatRecord(dataPath+"browse_history_train.txt",dataPath+"clean_browse_history_train.txt");
		// deleteRepeatRecord(dataPath+"bank_detail_train.txt",dataPath+"clean_bank_detail_train.txt");

		// 测试集：账单记录、浏览行为记录、银行流水记录去除重复数据
		// deleteRepeatRecord(testPath+"bill_detail_test.txt",testPath+"clean_bill_detail_test.txt");
		// deleteRepeatRecord(testPath+"browse_history_test.txt",testPath+"clean_browse_history_test.txt");
		// deleteRepeatRecord(testPath+"bank_detail_test.txt",
		// testPath+"clean_bank_detail_test.txt");

		// 合并同一笔工资收入、非工资收入和支出记录
		// combineBankRecord(dataPath+"bank_detail_train.txt",dataPath+"combine_bank_detail_train.txt");
		// combineBankRecord(testPath+"bank_detail_test.txt",testPath+"combine_bank_detail_test.txt");

		// 按照每个银行罗列用户的清单
		// billPerBank(dataPath+"clean_bill_detail_train.txt",dataPath+"perBillBank.txt");
		// billPerBank(testPath+"clean_bill_detail_test.txt",testPath+"perBillBank.txt");
		// billPerBank(NewdataPath+"Good_New_bill_detail.txt", NewdataPath+"Good_perBillBank1.txt");
		// billPerBank(NewdataPath+"Bad_New_bill_detail.txt", NewdataPath+"Bad_perBillBank1.txt");
		// billPerBank(dataPath+"clean_bill_detail_train.txt", NewdataPath+"cleanAgain_Train.txt");
		// billPerBank(testPath+"clean_bill_detail_test.txt", NewdataPath+"cleanAgain_Test.txt");
	}

	/**
	 * 按照每个银行罗列用户的清单
	 * @param billFileName
	 * @param billPerBankFileName
	 * @throws IOException
	 */
	public static void billPerBank(String billFileName, String billPerBankFileName) throws IOException {
		
		System.out.println("DataClean.billPerBank() begin execute");
		long beginTime = new Date().getTime();
		HashMap<String, ArrayList<BillDetail>> billDetailList = new HashMap<String, ArrayList<BillDetail>>(); // 每个用户对应的银行记录清单

		File billFile = new File(billFileName);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(billFile));
		String tempString = null;

		File billPerBankFile = new File(billPerBankFileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		fw = new FileWriter(billPerBankFile);
		writer = new BufferedWriter(fw);

		while ((tempString = reader.readLine()) != null) {
			String[] temp = tempString.split(",");
			ArrayList<BillDetail> billDetails; // 单个用户对应的银行记录清单

			if ((billDetails = billDetailList.get(temp[0])) == null) {
				billDetails = new ArrayList<BillDetail>();
			}

			billDetails.add(new BillDetail(temp[0], Long.valueOf(temp[1]), Integer.valueOf(temp[2]),
					Float.valueOf(temp[3]), Float.valueOf(temp[4]), Float.valueOf(temp[5]), Float.valueOf(temp[6]),
					Float.valueOf(temp[7]), Integer.valueOf(temp[8]), Float.valueOf(temp[9]), Float.valueOf(temp[10]),
					Float.valueOf(temp[11]), Float.valueOf(temp[12]), Float.valueOf(temp[13]),
					Integer.valueOf(temp[14])));
			billDetailList.put(temp[0], billDetails);
		}
		reader.close();

		Iterator<ArrayList<BillDetail>> iterator = billDetailList.values().iterator();
		while (iterator.hasNext()) {
			ArrayList<BillDetail> val = (ArrayList<BillDetail>) iterator.next();
			HashMap<Integer, ArrayList<BillDetail>> billMap = new HashMap<Integer, ArrayList<BillDetail>>(); // 分银行统计属性
			Iterator<BillDetail> it = val.iterator();
			while (it.hasNext()) {
				BillDetail tempDetail = it.next();
				ArrayList<BillDetail> billListOfEachBank; // 一个银行的信用账单记录
				if ((billListOfEachBank = billMap.get(tempDetail.bankID)) == null)
					billListOfEachBank = new ArrayList<BillDetail>();
				billListOfEachBank.add(tempDetail);
				billMap.put(tempDetail.bankID, billListOfEachBank);
			}
			Iterator<Entry<Integer, ArrayList<BillDetail>>> iter2 = billMap.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry<Integer, ArrayList<BillDetail>> entry2 = (Map.Entry<Integer, ArrayList<BillDetail>>) iter2.next();
				ArrayList<BillDetail> value = (ArrayList<BillDetail>) entry2.getValue();
				ArrayList<BillDetail> clean_value = cleanRepeatBill(BillResort.cleanAndfill(value));
				// 二次清洗账单数据后的账单时间戳填充
				for (int i = 0; i < clean_value.size(); i++)
					writer.write(clean_value.get(i).toString() + "\n");
			}
		}
		writer.flush();
		writer.close();
		long endTime = new Date().getTime();
		System.out.println("DataClean.billPerBank() execute time: " + (endTime - beginTime) / 1000 + "." + (endTime - beginTime) % 1000 + "s");
	}

	// 函数功能 ：去除冗余账单数据
	public static ArrayList<BillDetail> cleanRepeatBill(ArrayList<BillDetail> billList) {
		ArrayList<BillDetail> resultBillList = new ArrayList<BillDetail>();
		HashSet<String> billSet = new HashSet<String>();
		for (int i = 0; i < billList.size(); i++) {
			if (!billSet.contains(billList.get(i).toString())) {
				billSet.add(billList.get(i).toString());
				resultBillList.add(billList.get(i));
			}
		}
		return resultBillList;
	}

	// 函数功能 ：去掉Zombie账户
	public static int zombieBank(int bankId, ArrayList<BillDetail> billList) {
		int blankCount = 0;
		for (int i = 0; i < billList.size(); i++) // 判断是否为Zombie账户，从来没有过欠款、还款和消费行为
		{
			BillDetail curBillDetail = billList.get(i);
			if (curBillDetail.lastBill == 0 && curBillDetail.lastReturn == 0 && curBillDetail.thisBillRest == 0
					&& curBillDetail.consume == 0 && curBillDetail.thisBill == 0 && curBillDetail.adjustMoney == 0
					&& curBillDetail.cycleInterest == 0 && curBillDetail.borrowMoney == 0
					&& curBillDetail.useableMoney == 0)
				blankCount++;
		}
		if (blankCount == billList.size())
			return 0;
		else
			return 1;

	}

	/**
	 * 合并同一笔工资收入、非工资收入和支出记录
	 * @param bankRecordFileName
	 * @param combineBankRecordFileName
	 * @throws IOException
	 */
	public static void combineBankRecord(String bankRecordFileName, String combineBankRecordFileName) throws IOException {

		System.out.println("DataClean.combineBankRecord() begin execute");
		long beginTime = new Date().getTime();
		File bankRecordFile = new File(bankRecordFileName);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(bankRecordFile));
		String line = null;

		File combineBankRecordFile = new File(combineBankRecordFileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		fw = new FileWriter(combineBankRecordFile);
		writer = new BufferedWriter(fw);

		ArrayList<String> sameTimeComs = new ArrayList<String>();
		String curKey = new String();
		String saveKey = "init";
		while ((line = reader.readLine()) != null) {
			// 合并同一时间点下的同种类型的交易 : 支出、收入和额外收入
			float outputSum = 0f;
			float salaryInputSum = 0f;
			float elseInputSum = 0f; // 支出、收入和额外收入

			String[] temp = line.split(",");
			curKey = temp[0] + "," + temp[1]; // 用户ID+时间做键

			if (Long.valueOf(temp[1]) == 0L)
				writer.write(line + "\n"); // 时间戳未知的记录保留，不做处理
			else {
				if (curKey.compareTo(saveKey) == 0 || (saveKey == "init")) {
					sameTimeComs.add(line);
					saveKey = curKey;
				} else {
					String[] temp2 = new String[5];
					for (int index = 0; index < sameTimeComs.size(); index++) {
						String record = sameTimeComs.get(index);
						temp2 = record.split(",");

						if (Integer.valueOf(temp2[2]) == 1 && Integer.valueOf(temp2[4]) == 0) // 支出
							outputSum += Float.valueOf(temp2[3]);
						else if (Integer.valueOf(temp2[2]) == 0 && Integer.valueOf(temp2[4]) == 1) // 工资收入
							salaryInputSum += Float.valueOf(temp2[3]);
						else if (Integer.valueOf(temp2[2]) == 0 && Integer.valueOf(temp2[4]) == 0) // 其他收入
							elseInputSum += Float.valueOf(temp2[3]);
					}
					if (outputSum != 0f)
						writer.write(temp2[0] + "," + temp2[1] + ",1," + outputSum + ",0" + "\n");
					if (salaryInputSum != 0f)
						writer.write(temp2[0] + "," + temp2[1] + ",0," + salaryInputSum + ",1" + "\n");
					if (elseInputSum != 0f)
						writer.write(temp2[0] + "," + temp2[1] + ",0," + elseInputSum + ",0" + "\n");

					sameTimeComs.clear();
					sameTimeComs.add(line); // 新的记录 ：该用户下一时刻点或者新用户的记录
				}
				saveKey = line.split(",")[0] + "," + line.split(",")[1];
			}
		}
		writer.flush();
		reader.close();
		writer.close();
		long endTime = new Date().getTime();
		System.out.println("bank_detail size: " + sameTimeComs.size());
		System.out.println("DataClean.combineBankRecord() execute time: " + (endTime - beginTime) / 1000 + "." + (endTime - beginTime) % 1000 + "s");

	}

	/**
	 * 功能：清洗重复数据，目前只有信用卡账单数据需要清洗，银行流水记录和浏览行为不需要清洗！
	 * @param billTrainFileName 要去重的文件名
	 * @param cleanedBillFileName 结果存放文件名
	 * @throws IOException
	 */
	public static void deleteRepeatRecord(String billTrainFileName, String cleanedBillFileName) throws IOException {
		
		System.out.println("DataClean.deleteRepeatRecord() begin execute");
		long beginTime = new Date().getTime();
		HashSet<String> uniqueRecordList = new HashSet<String>();

		File billTrainFile = new File(billTrainFileName);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(billTrainFile));
		String line = null; // 读取的每行数据

		File cleanedBillFile = new File(cleanedBillFileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		fw = new FileWriter(cleanedBillFile);
		writer = new BufferedWriter(fw);

		while ((line = reader.readLine()) != null) {

			if (!uniqueRecordList.contains(line)) // 时间未知的数据保留，不好去冗余
			{
				uniqueRecordList.add(line);
				writer.write(line + "\n"); // 保持原来数据的相对时间顺序
			}
			
		}
		writer.flush();
		reader.close();
		writer.close();
		long endTime = new Date().getTime();
		System.out.println("bill size: " + uniqueRecordList.size());
		System.out.println("DataClean.deleteRepeatRecord() execute time: " + (endTime - beginTime) / 1000 + "." + (endTime - beginTime) % 1000 + "s");
		
	}

}
