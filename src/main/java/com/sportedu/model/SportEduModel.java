// SportEduModel.java
package com.sportedu.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportEduModel {

    // Data struktur untuk menyimpan informasi olahraga dan teknik
    private Map<String, List<String>> sportTechniques;
    private Map<String, TeknikInfo> teknikDetails;

    public SportEduModel() {
        initializeData();
    }

    private void initializeData() {
        // Inisialisasi data olahraga dan teknik-tekniknya
        sportTechniques = new HashMap<>();
        sportTechniques.put("SepakBola", Arrays.asList("Passing", "Dribbling", "Shooting", "Heading"));
        sportTechniques.put("Badminton", Arrays.asList("Servis", "Smash", "Dropshot", "Clear"));

        // Inisialisasi detail teknik
        teknikDetails = new HashMap<>();

        // Detail untuk teknik Passing
        teknikDetails.put("Passing", new TeknikInfo(
                "Passing",
                "Passing adalah keterampilan memindahkan bola dari satu pemain ke pemain lain, yang dapat dilakukan menggunakan kaki atau bagian tubuh lain kecuali tangan.",
                Arrays.asList("Bola", "Lapangan", "Sepatu bola"),
                Arrays.asList("Peralatan", "Animasi Passing", "Pengertian Passing")
        ));

        // Detail untuk teknik lainnya (bisa dikembangkan)
        teknikDetails.put("Dribbling", new TeknikInfo(
                "Dribbling",
                "Dribbling adalah teknik menggiring bola menggunakan kaki sambil berlari atau berjalan.",
                Arrays.asList("Bola", "Sepatu bola", "Cone/marker"),
                Arrays.asList("Teknik Dasar", "Variasi Dribbling", "Latihan")
        ));

        teknikDetails.put("Shooting", new TeknikInfo(
                "Shooting",
                "Shooting adalah teknik menendang bola ke arah gawang untuk mencetak gol.",
                Arrays.asList("Bola", "Gawang", "Sepatu bola"),
                Arrays.asList("Posisi Tubuh", "Teknik Tendangan", "Akurasi")
        ));

        teknikDetails.put("Heading", new TeknikInfo(
                "Heading",
                "Heading adalah teknik menyundul bola menggunakan kepala untuk mengoper atau mencetak gol.",
                Arrays.asList("Bola", "Sepatu bola"),
                Arrays.asList("Posisi Kepala", "Timing", "Arah Bola")
        ));
    }

    // Getter methods
    public List<String> getTechniquesBySport(String sport) {
        return sportTechniques.get(sport);
    }

    public TeknikInfo getTeknikInfo(String teknikName) {
        return teknikDetails.get(teknikName);
    }

    public List<String> getAllSports() {
        return Arrays.asList("SepakBola", "Badminton");
    }

    // Inner class untuk menyimpan informasi detail teknik
    public static class TeknikInfo {
        private String name;
        private String description;
        private List<String> equipment;
        private List<String> subPages;

        public TeknikInfo(String name, String description, List<String> equipment, List<String> subPages) {
            this.name = name;
            this.description = description;
            this.equipment = equipment;
            this.subPages = subPages;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<String> getEquipment() { return equipment; }
        public List<String> getSubPages() { return subPages; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setDescription(String description) { this.description = description; }
        public void setEquipment(List<String> equipment) { this.equipment = equipment; }
        public void setSubPages(List<String> subPages) { this.subPages = subPages; }
    }

    // Method untuk mendapatkan data quiz (akan dikembangkan nanti)
    public Map<String, Object> getQuizData() {
        Map<String, Object> quizData = new HashMap<>();
        // Implementasi data quiz akan ditambahkan nanti
        return quizData;
    }

    // Method untuk validasi data
    public boolean isValidSport(String sport) {
        return sportTechniques.containsKey(sport);
    }

    public boolean isValidTechnique(String sport, String technique) {
        List<String> techniques = sportTechniques.get(sport);
        return techniques != null && techniques.contains(technique);
    }
}