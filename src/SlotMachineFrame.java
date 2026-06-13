import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class SlotMachineFrame extends JFrame {

    // ===== 코인 =====
    int coin;

    // ===== UI =====
    private final JLabel slot1 = createSlotLabel();
    private final JLabel slot2 = createSlotLabel();
    private final JLabel slot3 = createSlotLabel();

    private final JLabel coinLabel = new JLabel();
    private final JLabel resultLabel =
            new JLabel("SPIN 버튼을 누르세요", SwingConstants.CENTER);

    private final JButton spinButton =
            new JButton("SPIN");

    // ===== 슬롯 =====
    private final String[] symbols = {"7", "A", "B"};
    private final Random random = new Random();

    private Timer timer;
    private int tick;

    private int reel1Index;
    private int reel2Index;
    private int reel3Index;

    public SlotMachineFrame() {

        // ===== 코인 로드 =====
        coin = loadCoin();

        setTitle("Slot Machine");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== 제목 =====
        JLabel title = new JLabel("SLOT MACHINE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        // ===== 슬롯 =====
        JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        slotPanel.add(slot1);
        slotPanel.add(slot2);
        slotPanel.add(slot3);
        add(slotPanel, BorderLayout.CENTER);

        // ===== 하단 =====
        JPanel bottom = new JPanel(new BorderLayout());

        coinLabel.setFont(new Font("Arial", Font.BOLD, 18));
        coinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateCoinLabel();

        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        spinButton.setFont(new Font("Arial", Font.BOLD, 20));
        spinButton.addActionListener(e -> startSpin());

        bottom.add(coinLabel, BorderLayout.NORTH);
        bottom.add(resultLabel, BorderLayout.CENTER);
        bottom.add(spinButton, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // ===== 종료 시 저장 =====
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveCoin();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    // ================= SLOT UI =================
    private JLabel createSlotLabel() {
        JLabel label = new JLabel("7", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(100, 100));
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        return label;
    }

    // ================= SPIN =================
    private void startSpin() {

        if (coin < 10) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("코인이 부족합니다");
            return;
        }

        coin -= 10;
        updateCoinLabel();

        spinButton.setEnabled(false);
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setText("회전 중...");

        tick = 0;

        reel1Index = random.nextInt(symbols.length);
        reel2Index = random.nextInt(symbols.length);
        reel3Index = random.nextInt(symbols.length);

        timer = new Timer(80, e -> {

            tick++;

            if (tick < 20) {
                slot1.setText(symbols[++reel1Index % symbols.length]);
            }

            if (tick < 35) {
                slot2.setText(symbols[++reel2Index % symbols.length]);
            }

            if (tick < 50) {
                slot3.setText(symbols[++reel3Index % symbols.length]);
            }

            if (tick >= 50) {
                timer.stop();
                checkResult();
                spinButton.setEnabled(true);
            }
        });

        timer.start();
    }

    // ================= RESULT =================
    private void checkResult() {

        String s1 = slot1.getText();
        String s2 = slot2.getText();
        String s3 = slot3.getText();

        if (s1.equals(s2) && s2.equals(s3)) {

            coin = (coin+10) * 2;
            resultLabel.setForeground(new Color(0, 150, 0));
            resultLabel.setText("SUCCESS +2X coin");

        } else {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("FAIL");
        }

        updateCoinLabel();
    }

    // ================= COIN UI =================
    private void updateCoinLabel() {
        coinLabel.setText("COIN : " + coin);
    }

    // ================= SAVE =================
    private void saveCoin() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("coin.txt"))) {
            bw.write(String.valueOf(coin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD =================
    private int loadCoin() {

        File file = new File("coin.txt");

        if (!file.exists()) return 500;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) return Integer.parseInt(line.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 500;
    }
}