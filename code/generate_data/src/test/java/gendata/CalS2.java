package gendata;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalS2 {
    public static void main(String[] args) {

        Map<Long, List<String>> map=new HashMap<>();
        try {
            InputStream inputStream = CalS2.class.getClassLoader().getResourceAsStream("cell_loc2.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] sps=line.split("\\|");
                String laccell=sps[0];
                double lat=Double.parseDouble(sps[1]);
                double lon=Double.parseDouble(sps[2]);
                long s2id=S2CellId.fromLatLng(S2LatLng.fromDegrees(lat, lon)).parent(13).id();
                List<String> list=map.getOrDefault(s2id,new ArrayList<>());
                list.add(laccell);
                map.put(s2id,list);
//                System.out.println(s2id+"|"+laccell);
            }

            map.entrySet().forEach(x-> {
                Long key=x.getKey();
                x.getValue().forEach(y-> System.out.println(key+"|"+y));
            });

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
