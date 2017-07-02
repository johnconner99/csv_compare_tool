import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class CurveCompare {
	public static void main(String [] args){
		CurveCSV curve = new CurveCSV();
		try {
			Map<CurveCSV, BigDecimal> curveUAT = curve.readCSVFile("F:\\practice\\Book1.csv");
			Map<CurveCSV, BigDecimal> curveProd = curve.readCSVFile("F:\\practice\\Book2.csv");

			curve.compareCSVFile(curveUAT, curveProd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
