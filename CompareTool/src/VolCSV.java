import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VolCSV {
	int volID;
	String volName;
	String curveDate;
	String expiryDate;
	String contractCode;
	BigDecimal Strike;
	BigDecimal vol;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Strike == null) ? 0 : Strike.hashCode());
		result = prime * result + ((contractCode == null) ? 0 : contractCode.hashCode());
		result = prime * result + ((curveDate == null) ? 0 : curveDate.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + volID;
		result = prime * result + ((volName == null) ? 0 : volName.hashCode());
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
		VolCSV other = (VolCSV) obj;
		if (Strike == null) {
			if (other.Strike != null)
				return false;
		} else if (!Strike.equals(other.Strike))
			return false;
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
		if (volID != other.volID)
			return false;
		if (volName == null) {
			if (other.volName != null)
				return false;
		} else if (!volName.equals(other.volName))
			return false;
		return true;
	}

	Map<VolCSV, BigDecimal> readCSVFile(String path) throws IOException {
		String[] line;
		String oneLiner;
		Map<VolCSV, BigDecimal> map = new HashMap<VolCSV, BigDecimal>();
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((oneLiner = br.readLine()) != null) {
			line = oneLiner.split(",");

			if (line[0].equalsIgnoreCase("curve_date")) {
				line = br.readLine().split(","); // skip the header
			}
			VolCSV volCSV = prepareCSVObject(line);
			map.put(volCSV, new BigDecimal(line[2]));
		}
		br.close();

		System.out.println(map);
		return map;
	}

	VolCSV prepareCSVObject(String[] line) {
		VolCSV volCSV = new VolCSV();
		volCSV.curveDate = line[0];
		volCSV.expiryDate = line[1];
		volCSV.vol = BigDecimal.valueOf(Double.parseDouble(line[2]));
		volCSV.Strike = BigDecimal.valueOf(Double.parseDouble(line[3]));
		volCSV.contractCode = line[4];
		volCSV.volName = line[5];
		volCSV.volID = Integer.parseInt(line[6]);

		return volCSV;

	}

	void prepareCSVReport(Map<VolCSV, BigDecimal> input,String destination) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
			bw.write("curve_date, expiry_date, vol, strike, contract_code, vol_name, vol_id ");
			bw.newLine();
			for (VolCSV volCSV : input.keySet()) {
				bw.write(volCSV.curveDate + "," + 
						volCSV.expiryDate + "," + 
						volCSV.vol + "," +
						volCSV.Strike + "," +
						volCSV.contractCode + "," + 
						volCSV.volName + "," + 
						volCSV.volID
				);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	void analyseMismatch(Map<VolCSV, BigDecimal> input) {

	}

	void analyseExtraUAT(Map<VolCSV, BigDecimal> input) {

	}

	void analyseExtraProd(Map<VolCSV, BigDecimal> input) {

	}

	void compareCSVFile(Map<VolCSV, BigDecimal> uat, Map<VolCSV, BigDecimal> prod) {
		Map<VolCSV, BigDecimal> match = new HashMap<VolCSV, BigDecimal>();
		Map<VolCSV, BigDecimal> mismatch = new HashMap<VolCSV, BigDecimal>();
		Map<VolCSV, BigDecimal> extraUAT = new HashMap<VolCSV, BigDecimal>();
		Map<VolCSV, BigDecimal> extraProd = new HashMap<VolCSV, BigDecimal>();

		for (VolCSV key : uat.keySet()) {
			if (prod.containsKey(key)) {
				if (prod.get(key).equals(uat.get(key))) {
					match.put(key, uat.get(key));
				} else {
					mismatch.put(key, uat.get(key));
				}
			} else {
				extraUAT.put(key, uat.get(key));
			}
		}

		for (VolCSV key : prod.keySet()) {
			if (!uat.containsKey(key)) {
				extraProd.put(key, prod.get(key));
			}
		}

		prepareCSVReport(match, "F:\\practice\\book_vol_match.csv");
		prepareCSVReport(mismatch, "F:\\practice\\book_vol_mismatch.csv");
		prepareCSVReport(extraProd, "F:\\practice\\book_vol_extraProd.csv");
		prepareCSVReport(extraUAT, "F:\\practice\\book_vol_extraUAT.csv");

	}

}
