import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author IlyaJava
 */
public enum StatusZakaza {
    NOVYI("НОВЫЙ", 24, 1),
    V_OBRABOTKE("В ОБРАБОТКЕ", 48, 6),
    PODTVERJDEN("ПОДТВЕРЖДЕН", 72, 12),
    OTPRAVLEN("ОТПРАВЛЕН", 168, 24),
    DOSTAVLEN("ДОСТАВЛЕН", 0, 0),
    OTMENEN("ОТМЕНЕН", 0, 0);

    private final String rusName;
    private final int srokJizVchasah;
    private final int periodObnovleniaVchasah;
    
    // Это поле НЕ final, так как будет изменяться для каждого экземпляра статуса
    private LocalDateTime dataSozd;  // Убрали final, добавили setter

    private StatusZakaza(String rusName, int srokJizVchasah, int periodObnovleniaVchasah) {
        this.rusName = rusName;
        this.srokJizVchasah = srokJizVchasah;
        this.periodObnovleniaVchasah = periodObnovleniaVchasah;
        // dataSozd не инициализируем здесь - будет устанавливаться при назначении статуса
    }

    public String getRusName() {
        return rusName;
    }

    public LocalDateTime getDataSozd() {
        return dataSozd;
    }

    // Добавляем метод setDataSozd
    public void setDataSozd(LocalDateTime dataSozd) {
        this.dataSozd = dataSozd;
    }

    public int getSrokJizVchasah() {
        return srokJizVchasah;
    }
    
    public int getPeriodObnovleniaVchasah() {
        return periodObnovleniaVchasah;
    }

    public boolean isIstek() {
        if (srokJizVchasah == 0 || dataSozd == null) return false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime krainiySrok = dataSozd.plus(srokJizVchasah, ChronoUnit.HOURS);
        return krainiySrok.isBefore(now);
    }
    
    public boolean trebuetObnovlenia(LocalDateTime posledneeObnovlenie) {
        if (periodObnovleniaVchasah == 0 || posledneeObnovlenie == null) return false;
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sleduyusheeObnovlenie = posledneeObnovlenie
            .plus(periodObnovleniaVchasah, ChronoUnit.HOURS);
        
        return now.isAfter(sleduyusheeObnovlenie) || now.equals(sleduyusheeObnovlenie);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - срок: %d ч., обновление: каждые %d ч.", 
            name(), rusName, srokJizVchasah, periodObnovleniaVchasah);
    }
}