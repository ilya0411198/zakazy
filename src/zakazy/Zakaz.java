package zakazy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
//  ДОБАВИЛИ ЭТУ СТРОЧКУ
//  ДОБАВИЛИ ЭТУ СТРОЧКУ

/**
 *
 * @author IlyaJava
 */
public class Zakaz {
    private int id;                          // уникальный номер заказа
    private String opisanie;                  // описание заказа
    private LocalDateTime dataSozdania;       // дата создания
    private StatusZakaza status;              // текущий статус
    private LocalDateTime dataObnovlenia;      // дата последнего изменения статуса

    public Zakaz(int id, String opisanie, LocalDateTime dataSozdania, StatusZakaza status, LocalDateTime dataObnovlenia) {
        this.id = id;
        this.opisanie = opisanie;
        this.dataSozdania = dataSozdania;
        this.status = status;
        this.dataObnovlenia = dataObnovlenia;
        
        // Устанавливаем время создания статуса
        if (this.status != null) {
            this.status.setDataSozd(this.dataSozdania);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public LocalDateTime getDataSozdania() {
        return dataSozdania;
    }

    public void setDataSozdania(LocalDateTime dataSozdania) {
        this.dataSozdania = dataSozdania;
    }

    public StatusZakaza getStatus() {
        return status;
    }

    public void setStatus(StatusZakaza status) {
        this.status = status;
        // Автоматически обновляем дату обновления при смене статуса
        this.dataObnovlenia = LocalDateTime.now();
        
        // Устанавливаем время создания нового статуса
        if (this.status != null) {
            this.status.setDataSozd(this.dataObnovlenia);
        }
    }

    public LocalDateTime getDataObnovlenia() {
        return dataObnovlenia;
    }

    public void setDataObnovlenia(LocalDateTime dataObnovlenia) {
        this.dataObnovlenia = dataObnovlenia;
    }
    
    // Новые методы для работы с обновлениями
    
    /**
     * Проверяет, требует ли заказ обновления
     */
    public boolean trebuetObnovlenia() {
        if (status == null || dataObnovlenia == null) {
            return false;
        }
        return status.trebuetObnovlenia(dataObnovlenia);
    }
    
    /**
     * Проверяет, истек ли срок жизни текущего статуса
     */
    public boolean isStatusIstek() {
        if (status == null) {
            return false;
        }
        return status.isIstek();
    }
    
    /**
     * Обновляет информацию о заказе (имитация обновления)
     */
    public void obnovitInformaciyu() {
        if (trebuetObnovlenia()) {
            this.dataObnovlenia = LocalDateTime.now();
            System.out.println("Информация по заказу #" + id + " обновлена");
        } else {
            System.out.println("Обновление не требуется для заказа #" + id);
        }
    }
    
    /**
     * Получает информацию о периоде обновления статуса
     */
    public String getPeriodObnovleniaInfo() {
        if (status == null) {
            return "Статус не установлен";
        }
        
        int period = status.getPeriodObnovleniaVchasah();
        if (period == 0) {
            return "Обновление не требуется";
        } else if (period == 1) {
            return "Обновлять каждый час";
        } else {
            return "Обновлять каждые " + period + " часов";
        }
    }
    
    /**
     * Получает время до следующего обновления
     */
    public String getVremyaDoSleduyushegoObnovlenia() {
        if (status == null || dataObnovlenia == null) {
            return "Нет данных";
        }
        
        int period = status.getPeriodObnovleniaVchasah();
        if (period == 0) {
            return "Обновление не требуется";
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sleduyusheeObnovlenie = dataObnovlenia.plusHours(period);
        
        if (now.isAfter(sleduyusheeObnovlenie)) {
            return "Требуется обновление";
        }
        
        long chasov = java.time.temporal.ChronoUnit.HOURS.between(now, sleduyusheeObnovlenie);
        long minut = java.time.temporal.ChronoUnit.MINUTES.between(now, sleduyusheeObnovlenie) % 60;
        
        return String.format("%d ч. %d мин.", chasov, minut);
    }
    
    /**
     * Проверяет возможность перехода в новый статус
     */
    public boolean mozhetPereytiV(StatusZakaza noviyStatus) {
        if (this.status == null) {
            return true;
        }
        
        // Нельзя перейти в тот же статус
        if (this.status == noviyStatus) {
            System.out.println("Заказ уже имеет статус " + noviyStatus.getRusName());
            return false;
        }
        
        // Проверка на истекший срок
        if (this.status.isIstek()) {
            if (noviyStatus == StatusZakaza.OTMENEN) {
                return true; // Можно отменить просроченный заказ
            } else {
                System.out.println("Срок действия статуса истек. Можно только отменить заказ.");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Переводит заказ в новый статус с проверками
     */
    public boolean perevestiVStatus(StatusZakaza noviyStatus) {
        if (!mozhetPereytiV(noviyStatus)) {
            return false;
        }
        
        setStatus(noviyStatus);
        System.out.println("Заказ #" + id + " переведен в статус: " + noviyStatus.getRusName());
        return true;
    }
    
    /**
     * Получает полную информацию о заказе
     */
    public String getFullInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        
        StringBuilder info = new StringBuilder();
        info.append("=== Заказ #").append(id).append(" ===\n");
        info.append("Описание: ").append(opisanie).append("\n");
        info.append("Дата создания: ").append(dataSozdania.format(formatter)).append("\n");
        
        if (status != null) {
            info.append("Статус: ").append(status.getRusName()).append("\n");
            info.append("Срок жизни статуса: ").append(status.getSrokJizVchasah()).append(" часов\n");
            info.append("Период обновления: ").append(getPeriodObnovleniaInfo()).append("\n");
            info.append("Статус просрочен: ").append(isStatusIstek() ? "Да" : "Нет").append("\n");
        } else {
            info.append("Статус: не установлен\n");
        }
        
        info.append("Последнее обновление: ").append(dataObnovlenia.format(formatter)).append("\n");
        info.append("Время до след. обновления: ").append(getVremyaDoSleduyushegoObnovlenia());
        
        return info.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Заказ #%d: %s [%s]", id, opisanie, 
            status != null ? status.getRusName() : "нет статуса");
    }
}