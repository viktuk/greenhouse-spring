package xyz.viktuk.greenhouse;

import xyz.viktuk.greenhouse.processor.SimulationProcessor;

import java.math.BigInteger;

import static xyz.viktuk.greenhouse.Constants.TICK_INTERVAL;

public class SimulationThread extends Thread {
    private final BigInteger projectId;
    private final SimulationProcessor simulationProcessor;


    public SimulationThread(BigInteger projectId, SimulationProcessor simulationProcessor) {
        this.projectId = projectId;
        this.simulationProcessor = simulationProcessor;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            simulationProcessor.tick(projectId);
            try {
                Thread.sleep(TICK_INTERVAL);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
