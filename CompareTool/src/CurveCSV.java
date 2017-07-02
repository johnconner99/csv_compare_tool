import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurveCSV{
	int indexID;
	String indexName;
	String curveDate;
	String expiryDate;
	String contractCode;
	BigDecimal price;
	
	CurveCSV prepareCurveObject(String [] line){
		CurveCSV curveCSV = new CurveCSV();
		curveCSV.curveDate = line[0];
		curveCSV.expiryDate = line[1];
		curveCSV.price = BigDecimal.valueOf(Double.parseDouble(line[2]));
		curveCSV.contractCode = line[4];
		curveCSV.indexName = line[5];
		curveCSV.indexID = Integer.parseInt(line[6]);

		return curveCSV;		
	}
	
	Map<CurveCSV,BigDecimal> readCSVFile(String path) throws IOException {
		String[] line;
		String oneLiner;
		Map<CurveCSV,BigDecimal> map = new HashMap<CurveCSV,BigDecimal>();
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		while((oneLiner = br.readLine()) != null){
			line = oneLiner.split(",");
			
			if(line[0].equalsIgnoreCase("curve_date")){
				line = br.readLine().split(",");	//skip the header	
			}
			CurveCSV curveCSV = prepareCurveObject(line);
			map.put(curveCSV, new BigDecimal(line[2]));
		}
		br.close();		
		System.out.println(map);
		return map;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractCode == null) ? 0 : contractCode.hashCode());
		result = prime * result + ((curveDate == null) ? 0 : curveDate.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + indexID;
		result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurveCSV other = (CurveCSV) obj;
		if (contractCode == null) {
			if (other.contractCode != null)
				return false;
		} else if (!contractCode.equals(other.contractCode))
			return false;
		if (curveDate == null) {
			if (other.curveDate != null)
				return false;
		} else if (!curveDate.equals(other.curveDate))
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (indexID != other.indexID)
			return false;
		if (indexName == null) {
			if (other.indexName != null)
				return false;
		} else if (!indexName.equals(other.indexName))
			return false;
		return true;
	}

	void prepareCSVReport(Map<CurveCSV,BigDecimal> input, String destination) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
			bw.write("curve_date, expiry_date, price, contract_code, index_name, index_id ");
			bw.newLine();
			for (CurveCSV curveCSV : input.keySet()) {
				bw.write(curveCSV.curveDate + "," + 
						curveCSV.expiryDate + "," + 
						curveCSV.price + "," +
						curveCSV.contractCode + "," + 
						curveCSV.indexName + "," + 
						curveCSV.indexID
				);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

	}
	
	void analyseMismatch(Map<CurveCSV,BigDecimal> input){
		
	}
	
	void analyseExtraUAT(Map<CurveCSV,BigDecimal> input){
		
	}

	void analyseExtraProd(Map<CurveCSV,BigDecimal> input){
		
	}
	void compareCSVFile(Map<CurveCSV,BigDecimal> uat, Map<CurveCSV,BigDecimal> prod) {
		Map<CurveCSV,BigDecimal> match = new HashMap<CurveCSV,BigDecimal>();
		Map<CurveCSV,BigDecimal> mismatch= new HashMap<CurveCSV,BigDecimal>();
		Map<CurveCSV,BigDecimal> extraUAT= new HashMap<CurveCSV,BigDecimal>();
		Map<CurveCSV,BigDecimal> extraProd= new HashMap<CurveCSV,BigDecimal>();
		
		for(CurveCSV key:uat.keySet()){
			if(prod.containsKey(key)){
				if(prod.get(key).equals(uat.get(key))){
					match.put(key, uat.get(key));					
				}else{
					mismatch.put(key, uat.get(key));
				}			
			}
			else{
				extraUAT.put(key, uat.get(key));
			}
		}
		
		for(CurveCSV key:prod.keySet()){
			if(!uat.containsKey(key)){
				extraProd.put(key, prod.get(key));
			}
			
		}
		
		prepareCSVReport(match, "F:\\practice\\book_vol_match.csv");
		prepareCSVReport(mismatch, "F:\\practice\\book_vol_mismatch.csv");
		prepareCSVReport(extraProd, "F:\\practice\\book_vol_extraProd.csv");
		prepareCSVReport(extraUAT, "F:\\practice\\book_vol_extraUAT.csv");

	}
	
	

}
