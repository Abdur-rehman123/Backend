package KirayoApp.Kirayo.returnStatus;

import java.time.LocalDate;

public class ReservationResponse {
    private LocalDate startedAt;
    private LocalDate endedAt;

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDate getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDate endedAt) {
        this.endedAt = endedAt;
    }
}
