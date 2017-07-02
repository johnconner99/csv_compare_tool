import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class VolCompare {
	public static void main(String [] args){
		VolCSV vol = new VolCSV();
		try {
			
			Map<VolCSV, BigDecimal> volUAT = vol.readCSVFile("F:\\practice\\book_vol_uat.csv");
			Map<VolCSV, BigDecimal> volProd = vol.readCSVFile("F:\\practice\\book_vol_prod.csv");

			vol.compareCSVFile(volUAT, volProd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
