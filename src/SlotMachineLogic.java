import java.util.Random;

public class SlotMachineLogic {

    private final Random random = new Random();

    public int[] spin() {
        return new int[] {
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10)
        };
    }

    public boolean isSuccess(int[] slots) {
        return slots[0] == slots[1]
                && slots[1] == slots[2];
    }
}