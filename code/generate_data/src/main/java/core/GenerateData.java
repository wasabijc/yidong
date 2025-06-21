package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据生成器主类，用于模拟用户移动位置数据
 */
public class GenerateData {

    /**
     * 主方法，程序入口
     * @param args 命令行参数
     * @throws IOException 文件读取异常
     * @throws InterruptedException 线程中断异常
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. 从文件加载用户基本信息和基站位置信息
        List<String> userInfoLines = loadUserData("user_info.txt");
        List<CellInfo> cellLocLines = loadCellData("cell_loc.txt");

        // 2. 为每个基站分配索引并排序
        indexCellLocations(cellLocLines);

        // 3. 创建基站索引映射表
        Map<Integer, CellInfo> indexToCellMap = createIndexToCellMap(cellLocLines);
        Map<String, Integer> cellToIndexMap = createCellToIndexMap(cellLocLines);

        // 4. 记录用户当前所在的基站
        Map<String, String> userCurrentCell = new HashMap<>();

        // 5. 主循环：持续生成模拟数据
        generateSimulationData(userInfoLines, indexToCellMap, cellToIndexMap, userCurrentCell);
    }

    /**
     * 从文件加载用户数据
     * @param filePath 文件路径
     * @return 用户数据行列表
     * @throws IOException 文件读取异常
     */
    private static List<String> loadUserData(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        GenerateData.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 从文件加载基站数据并进行初步处理
     * @param filePath 文件路径
     * @return 基站信息列表
     * @throws IOException 文件读取异常
     */
    private static List<CellInfo> loadCellData(String filePath) throws IOException {
        return loadUserData(filePath).stream()
                .map(line -> {
                    String[] parts = line.split("\\|");
                    CellInfo cell = new CellInfo();
                    cell.cell = parts[0];    // 基站ID
                    cell.lat = parts[1];     // 纬度
                    cell.lot = parts[2];     // 经度
                    return cell;
                })
                .sorted(Comparator.comparingLong(c -> Long.parseLong(c.cell))) // 按基站ID排序
                .collect(Collectors.toList());
    }

    /**
     * 为基站分配索引
     * @param cells 基站列表
     */
    private static void indexCellLocations(List<CellInfo> cells) {
        for (int i = 0; i < cells.size(); i++) {
            cells.get(i).index = i;
        }
    }

    /**
     * 创建基站索引到基站对象的映射
     * @param cells 基站列表
     * @return 映射表
     */
    private static Map<Integer, CellInfo> createIndexToCellMap(List<CellInfo> cells) {
        return cells.stream()
                .collect(Collectors.toMap(c -> c.index, c -> c));
    }

    /**
     * 创建基站ID到索引的映射
     * @param cells 基站列表
     * @return 映射表
     */
    private static Map<String, Integer> createCellToIndexMap(List<CellInfo> cells) {
        return cells.stream()
                .collect(Collectors.toMap(c -> c.cell, c -> c.index));
    }

    /**
     * 生成模拟数据的主循环
     * @param userInfo 用户信息列表
     * @param indexToCell 基站索引映射
     * @param cellToIndex 基站ID映射
     * @param userCurrentCell 记录用户当前位置
     * @throws InterruptedException 线程中断异常
     */
    private static void generateSimulationData(List<String> userInfo,
                                               Map<Integer, CellInfo> indexToCell,
                                               Map<String, Integer> cellToIndex,
                                               Map<String, String> userCurrentCell) throws InterruptedException {

        Random random = new Random();

        // 持续生成数据直到程序被终止
        while (true) {
            // 随机选择一个用户
            String randomUser = userInfo.get(random.nextInt(userInfo.size()));
            String[] userParts = randomUser.split("\\|");
            String imsi = userParts[0];      // 用户IMSI
            String gender = userParts[1];    // 性别 (0:未知,1:男,2:女)
            String age = userParts[2];       // 年龄

            // 获取用户当前所在的基站索引，如果不存在则随机分配一个
            int lastIndex = cellToIndex.getOrDefault(
                    userCurrentCell.get(imsi),
                    random.nextInt(indexToCell.size()));

            // 生成移动步长 (-3到3之间的随机数)
            int step = random.nextInt(7) - 3;

            // 只有当步长为正时才移动
            if (step > 0) {
                // 计算可能的移动方向
                int[] possibleMoves = {
                        lastIndex + step < indexToCell.size() ? lastIndex + step : lastIndex - step,
                        lastIndex - step > 0 ? lastIndex - step : lastIndex + step
                };

                // 随机选择一个方向移动
                int nextIndex = possibleMoves[random.nextInt(2)];
                CellInfo nextCell = indexToCell.get(nextIndex);

                // 更新用户当前位置
                userCurrentCell.put(imsi, nextCell.cell);

                // 生成数据记录
                long currentTime = System.currentTimeMillis();
                String record = String.join("|",
                        imsi,            // 用户ID
                        nextCell.cell,    // 基站ID
                        nextCell.lat,     // 纬度
                        nextCell.lot,     // 经度
                        String.valueOf(currentTime), // 时间戳
                        gender,           // 性别
                        age               // 年龄
                );

                // 输出生成的记录
                System.out.println(record);
            }

            // 暂停100毫秒模拟实时数据
            Thread.sleep(100);
        }
    }
}

/**
 * 基站信息类，存储单个基站的数据
 */
class CellInfo {
    public String cell;  // 基站ID
    public String lat;   // 纬度
    public String lot;   // 经度
    public int index;    // 索引位置

    /**
     * 默认构造函数
     */
    public CellInfo() {
    }

    /**
     * 带索引的构造函数
     * @param index 基站索引
     */
    public CellInfo(int index) {
        this.index = index;
    }

    /**
     * 转换为字符串表示
     * @return 格式化的基站信息
     */
    @Override
    public String toString() {
        return String.join("|", cell, lat, lot, String.valueOf(index));
    }
}