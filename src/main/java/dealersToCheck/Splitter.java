package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Splitter {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber, String pageURL, int startingPosition, int nPosPerCat, String country){
		List<List<Object>> report = new ArrayList<List<Object>>();
		switch (dealer) {
        case "Thomann": try {
				report = Thomann.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                 break;
        case "ThomannBest": try {
			report = ThomannBest.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             break;
        case "MusicStore": try {
				report = MusicStore.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		 break;
        case "Miroc": try {
			report = Miroc.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;
        case "Andertons": try {
			report = Andertons.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;    		 
        case "Bax": try {
			report = Bax.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
        case "VintageKing": try {
			report = VintageKing.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "LongMcQuade": try {
			report = LongMcQuade.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "ZZsounds": try {
			report = ZZsounds.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "Woodbrass": try {
			report = Woodbrass.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "Gear4music": try {
			report = Gear4music.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "IkebeGakki": try {
			report = IkebeGakki.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
        case "StrumentiMusicali": try {
			report = StrumentiMusicali.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
        case "TurraMusic": try {
			report = TurraMusic.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
        case "StudioEconomik": try {
			report = StudioEconomik.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "Sweetwater": try {
			report = Sweetwater.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "WestlakePro": try {
			report = WestlakePro.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
    		 
        case "BandH": try {
    		report = BandH.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 
        
        case "Microfusa": try {
    		report = Microfusa.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 	 
    		 
        case "AltoMusic": try {
    		report = AltoMusic.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 	 
    		 
        case "Rspe": try {
    		report = Rspe.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 	
    		 
        case "SoundHouse": try {
    		report = SoundHouse.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition,nPosPerCat,country);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		 break;	 	 
    		 
        default: report = null;
        break;
		}		
		return report;		
	}

}
