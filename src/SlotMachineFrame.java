import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class SlotMachineFrame extends JFrame {

    // 저장 파일명, 시작 코인, 스핀 비용, 슬롯에 표시될 기호
    private static final String SAVE_FILE = "coin.txt";
    private static final int START_COIN = 500;
    private static final int SPIN_COST = 10;
    private static final String[] SYMBOLS = {"7", "A", "B"};

    // 현재 보유 코인
    private int coin = loadCoin();

    // 랜덤 슬롯 출력을 위한 객체
    private final Random random = new Random();

    // 슬롯 화면 라벨 3개
    private final JLabel slot1 = createSlotLabel();
    private final JLabel slot2 = createSlotLabel();
    private final JLabel slot3 = createSlotLabel();

    // 코인, 결과 메시지, 버튼 UI
    private final JLabel coinLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel resultLabel =
            new JLabel("SPIN 버튼을 누르세요", SwingConstants.CENTER);

    private final JButton spinButton = new JButton("SPIN");

    public SlotMachineFrame() {
        // 기본 창 설정
        setTitle("Slot Machine");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // 제목 라벨 생성
        JLabel title = new JLabel("SLOT MACHINE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        // 슬롯 3개를 가운데 영역에 배치
        JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        slotPanel.add(slot1);
        slotPanel.add(slot2);
        slotPanel.add(slot3);
        add(slotPanel, BorderLayout.CENTER);

        // 하단 영역 생성
        JPanel bottom = new JPanel(new BorderLayout());

        coinLabel.setFont(new Font("Arial", Font.BOLD, 18));
        updateCoinLabel();

        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        // 버튼 클릭 시 슬롯 회전 시작
        spinButton.setFont(new Font("Arial", Font.BOLD, 20));
        spinButton.addActionListener(e -> startSpin());

        bottom.add(coinLabel, BorderLayout.NORTH);
        bottom.add(resultLabel, BorderLayout.CENTER);
        bottom.add(spinButton, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // 창을 닫을 때 코인을 저장하거나 저장 파일 삭제
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (coin == 0) {
                    deleteCoinFile();
                } else {
                    saveCoin();
                }

                System.exit(0);
            }
        });

        setVisible(true);
    }

    // 슬롯 하나의 UI 라벨 생성
    private JLabel createSlotLabel() {
        JLabel label = new JLabel("7", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(100, 100));
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        return label;
    }

    // 슬롯 회전 시작
    private void startSpin() {
        // 코인이 0이면 게임 진행 불가
        if (coin <= 0) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("코인이 0입니다. 게임을 종료하면 저장 파일이 삭제됩니다.");
            return;
        }

        // 스핀 비용보다 코인이 적으면 진행 불가
        if (coin < SPIN_COST) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("코인이 부족합니다");
            return;
        }

        // 스핀 비용 차감
        coin -= SPIN_COST;
        updateCoinLabel();

        spinButton.setEnabled(false);
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setText("회전 중...");

        // Timer 안에서 값을 변경하기 위해 배열 사용
        int[] tick = {0};

        // 0.08초마다 슬롯 값을 변경
        Timer spinTimer = new Timer(80, null);
        spinTimer.addActionListener(e -> {
            tick[0]++;

            // 각 슬롯이 서로 다른 시점까지 회전
            if (tick[0] < 20) {
                slot1.setText(randomSymbol());
            }

            if (tick[0] < 35) {
                slot2.setText(randomSymbol());
            }

            if (tick[0] < 50) {
                slot3.setText(randomSymbol());
            }

            // 회전 종료 후 결과 확인
            if (tick[0] >= 50) {
                ((Timer) e.getSource()).stop();
                checkResult();
                spinButton.setEnabled(true);
            }
        });

        spinTimer.start();
    }

    // 랜덤 슬롯 기호 반환
    private String randomSymbol() {
        return SYMBOLS[random.nextInt(SYMBOLS.length)];
    }

    // 슬롯 결과 확인
    private void checkResult() {
        String s1 = slot1.getText();
        String s2 = slot2.getText();
        String s3 = slot3.getText();

        // 세 슬롯이 모두 같으면 성공
        if (s1.equals(s2) && s2.equals(s3)) {
            coin = (coin + SPIN_COST) * 2;
            resultLabel.setForeground(new Color(0, 150, 0));
            resultLabel.setText("SUCCESS +2X coin");
        } else {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("FAIL");
        }

        updateCoinLabel();
    }

    // 코인 라벨 갱신
    private void updateCoinLabel() {
        coinLabel.setText("COIN : " + coin);
    }

    // 현재 코인을 파일에 저장
    private void saveCoin() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            bw.write(String.valueOf(coin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 코인이 0일 때 저장 파일 삭제
    private void deleteCoinFile() {
        File file = new File(SAVE_FILE);

        if (file.exists() && !file.delete()) {
            System.out.println(SAVE_FILE + " 삭제 실패");
        }
    }

    // 저장된 코인을 불러옴
    private int loadCoin() {
        File file = new File(SAVE_FILE);

        // 저장 파일이 없으면 기본 코인 지급
        if (!file.exists()) {
            return START_COIN;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // 파일을 읽지 못하면 기본 코인 지급
        return START_COIN;
    }
}