package pinride.minhna.submission.ambulancenow.map;

import java.util.HashMap;
import java.util.Map;

import pinride.minhna.submission.ambulancenow.module.Place;

/**
 * Created by Minh on 3/26/2016.
 */
public class ambLoc {
    public static Map<String, Place> ambMaps;

    public static Map<String, Place> ambLoc() {
        if (ambMaps!=null)
            return ambMaps;
        ambMaps =new HashMap<>();
        ambMaps.put("ambulance_chasis", new Place("Nguyễn Văn Linh, Phú Chánh, Tân Uyên, Bình Dương, Vietnam", 11.064197, 106.692297));
        ambMaps.put("mini_amb", new Place("Chùa Thiên Hậu Thánh Mẫu", 11.065399, 106.687271));
        ambMaps.put("ambulance_2", new Place("Lô C13 Đường Hùng Vương Khu Liên Hợp, Thủ Dầu Một, Bình Dương", 11.049805, 106.680869));
        return ambMaps;
    }
}
