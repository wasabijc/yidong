package gendata;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateData {


    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> userInfoLines=getDataFromFile("user_info.txt");
        List<CellInfo> cellLocLines=getDataFromFile("cell_loc.txt").stream().map(x->{
            String [] cellLocLine_split=x.split("\\|");
            CellInfo cellinfo=new CellInfo();
            cellinfo.cell=cellLocLine_split[0];
            cellinfo.lat=cellLocLine_split[1];
            cellinfo.lot=cellLocLine_split[2];

            return cellinfo;
        }).sorted(Comparator.comparingLong(x->Long.parseLong(x.cell))).collect(Collectors.toList());


        for(int i=0;i<cellLocLines.size();i++){
            cellLocLines.get(i).index=i;

        }
        Map<String,CellInfo> cellLocMap=cellLocLines.stream().collect(Collectors.toMap(x->x.cell,x->x));

        Map<String, String> userStatus = new HashMap<>();

        Random r=new Random();
        try(
                ServerSocket serverSocket = new ServerSocket(5000);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


                ){
            while(true){
                //随机获取一个用户的imsi
                String ranUserline=userInfoLines.get(r.nextInt(userInfoLines.size()));
                String [] ranUserline_split=ranUserline.split("\\|");
                String imsi=ranUserline_split[0];

                int lastIndex=cellLocMap.getOrDefault(userStatus.get(imsi),new CellInfo(r.nextInt(cellLocLines.size()))).index;
                int step=r.nextInt(7)-3;
                System.out.println("step : "+step);
                if(step>0){
                      int[] chose={lastIndex+step<cellLocLines.size()?lastIndex+step:lastIndex-step,lastIndex-step>0?lastIndex-step:lastIndex+step};
                      int nextIndex=chose[r.nextInt(2)];

                    System.out.println("nextIndex : "+nextIndex);
                    CellInfo nextcell=cellLocMap.get(nextIndex);
                    userStatus.put(imsi,nextcell.cell);
                    //生成进入基站时间
                    //生成进入基站时间
                    long currTime=System.currentTimeMillis();

                    String data=imsi+"|"+nextcell.cell+"|"+nextcell.lat+"|"+nextcell.lot+"|"+currTime;

                    out.println( data);
                    System.out.println(data);


                }


                Thread.sleep(1000);



            }
        }



    }
    private static List<String> getDataFromFile(String filePath) throws IOException {
        List<String> list= new ArrayList<>();

        try(BufferedReader br=new BufferedReader(new InputStreamReader(GenerateData.class.getClassLoader().getResourceAsStream(filePath)))){
            String line;
            while((line=br.readLine())!=null){
                list.add(line);
            }
        }
        return list;
    }
}

class CellInfo{
    public String cell;
    public String lat;
    public String lot;
    public int index;
    public CellInfo(){

    }
    public CellInfo(int index){
        this.index=index;
    }
    @Override
    public String toString() {
        return cell+"|"+lat+"|"+lot+"|"+index;
    }
}