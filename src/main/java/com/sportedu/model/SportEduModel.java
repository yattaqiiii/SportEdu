package com.sportedu.model;

import java.util.*;

public class SportEduModel {

    private final List<Soal> soalList;
    private final Map<String, List<Teknik>> dataMateri;
    private final List<SoalMencocokkan> soalMencocokkanList;

    public SportEduModel() {
        soalList = new ArrayList<>();
        dataMateri = new HashMap<>();
        soalMencocokkanList = new ArrayList<>();
        loadSoalData();
        loadMateriData();
        loadSoalMencocokkanData();
    }

    private void loadMateriData() {
        List<Teknik> sepakBolaTeknik = new ArrayList<>();
        sepakBolaTeknik.add(new Teknik("Passing", "Passing adalah teknik mengoper bola ke rekan satu tim.", "/assets/gif1.webp"));
        sepakBolaTeknik.add(new Teknik("Dribbling", "Dribbling adalah teknik menggiring bola melewati lawan.", "/assets/gif2.webp"));
        sepakBolaTeknik.add(new Teknik("Shooting", "Shooting adalah teknik menendang bola ke arah gawang.", "/assets/gif3.webp"));
        sepakBolaTeknik.add(new Teknik("Heading", "Heading adalah teknik menyundul bola menggunakan kepala.", "/assets/gif4.webp"));
        dataMateri.put("Sepak Bola", sepakBolaTeknik);

        List<Teknik> badmintonTeknik = new ArrayList<>();
        badmintonTeknik.add(new Teknik("Servis", "Servis adalah pukulan awal untuk memulai permainan.", "/assets/gif5.webp"));
        badmintonTeknik.add(new Teknik("Smash", "Smash adalah pukulan keras dan menukik tajam.", "/assets/gif6.webp"));
        badmintonTeknik.add(new Teknik("Footwork", "Footwork adalah pergerakan kaki yang efisien.", "/assets/gif7.webp"));
        badmintonTeknik.add(new Teknik("Netting", "Netting adalah pukulan pelan di dekat net.", "/assets/gif18.webp"));
        dataMateri.put("Badminton", badmintonTeknik);
    }

    private void loadSoalData() {
        soalList.add(new Soal("/assets/soccer_dribble.jpg", new String[]{"Dribbling", "Passing", "Heading", "Shooting"}, "Dribbling"));
        soalList.add(new Soal("/assets/badminton_smash.jpg", new String[]{"Servis", "Smash", "Netting", "Footwork"}, "Smash"));
        soalList.add(new Soal("/assets/soccer_shoot.jpg", new String[]{"Tackle", "Goal", "Shooting", "Goalkeeper"}, "Shooting"));
        soalList.add(new Soal("/assets/badminton_service.jpg", new String[]{"Servis", "Netting", "Smash", "Drop shot"}, "Servis"));
        soalList.add(new Soal("/assets/soccer_heading.jpg", new String[]{"Passing", "Dribbling", "Heading", "Tackle"}, "Heading"));
        soalList.add(new Soal("/assets/badminton_netting.jpg", new String[]{"Lob", "Smash", "Netting", "Servis"}, "Netting"));
    }

    private void loadSoalMencocokkanData() {
        // Set 1
        Map<String, String> set1 = new HashMap<>();
        set1.put("/assets/soccer_dribble.jpg", "Dribbling");
        set1.put("/assets/badminton_smash.jpg", "Smash");
        set1.put("/assets/soccer_heading.jpg", "Heading");
        set1.put("/assets/badminton_service.jpg", "Servis");
        soalMencocokkanList.add(new SoalMencocokkan(set1));

        // Set 2
        Map<String, String> set2 = new HashMap<>();
        set2.put("/assets/soccer_shoot.jpg", "Shooting");
        set2.put("/assets/badminton_netting.jpg", "Netting");
        set2.put("/assets/soccer_tackle.jpg", "Tackle");
        set2.put("/assets/soccer_goalkeeper.jpg", "Goalkeeper");
        soalMencocokkanList.add(new SoalMencocokkan(set2));
    }

    public List<Soal> getShuffledSoalList() {
        List<Soal> shuffledList = new ArrayList<>(soalList);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

    public List<SoalMencocokkan> getSoalMencocokkanList() {
        return soalMencocokkanList;
    }

    public List<Teknik> getTeknikList(String olahraga) {
        return dataMateri.getOrDefault(olahraga, new ArrayList<>());
    }
}
