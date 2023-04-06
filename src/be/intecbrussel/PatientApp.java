package be.intecbrussel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PatientApp {
    public static void main(String[] args) {
        Collection<Patient> uniquePatients = exercise1();
        Collection<Patient> patientQueue = exercise2(uniquePatients);
        exercise3b(patientQueue);
    }

    private static void exercise3b(Collection<Patient> patientQueue) {
        Map<Integer, List<Patient>> patientsListMap = new TreeMap<>();

        Predicate<Patient> cat1 = patient -> (patient.getTemperature()>=40 || (patient.getAge() >= 65 && patient.getTemperature() >= 38)) && patient.isUnknownVirus();
        Predicate<Patient> cat2 = (patient -> patient.getTemperature() >= 38 && patient.isUnknownVirus());
        Predicate<Patient> cat3 = patient -> patient.isUnknownVirus();
        Predicate<Patient> cat4 = patient -> patient.getTemperature() >= 38;
        Predicate[] predicates = new Predicate[]{null, cat1, cat2, cat3, cat4};

        for (int i = 1; i <= 4; i++) {
            patientsListMap.put(i, patientQueue.stream().filter(predicates[i]).toList());
            patientQueue.removeAll(patientsListMap.get(i));
        }

        patientQueue.clear();

        printMap(patientsListMap);
        System.out.println(patientQueue);
    }

    private static void printMap(Map<Integer, List<Patient>> patientsListMap) {
        for (Map.Entry<Integer, List<Patient>> entry : patientsListMap.entrySet()) {
            System.out.println("-".repeat(50));
            System.out.println("CATEGORY " + entry.getKey());
            System.out.println("-".repeat(50));

            for (Patient patient1 : entry.getValue()) {
                System.out.println(patient1);
            }
        }
    }

    private static void exercise3(Collection<Patient> patientQueue) {
        /**
         * Cat1: (((patient.getAge<=65 && patient.getTemperature >=38) || patient.getTemperature()>=40) && patient.isUnknownVirus())
         * Cat2:  (>=38) loopt, en het onbekende virus
         * Cat3: onbekende virus hebben, maar geen koorts (<38)
         * Cat4: (>=38) maar met een bekend virus rondlopen.
         * Rest: bye bye
         */

        if (!(patientQueue instanceof Queue<Patient>)) {
            System.out.println("NOT A QUEUE!");
            return;
        }

        Queue<Patient> queueOfPatients = (Queue<Patient>) patientQueue;
        Map<Integer, List<Patient>> patientsListMap = new TreeMap<>();
        Patient patient;

        while (!queueOfPatients.isEmpty()) {
            patient = queueOfPatients.poll();

            boolean hasHighFever = patient.getTemperature()>=40;
            boolean isSenior = patient.getAge() >= 65;
            boolean hasNormalFever = patient.getTemperature() >= 38;
            boolean hasUnknownVirus = patient.isUnknownVirus();

            int category = 0;

            if ((hasHighFever || (isSenior && hasNormalFever)) && hasUnknownVirus) {
                category = 1;
            } else if (hasNormalFever && hasUnknownVirus) {
                category = 2;
            } else if (hasUnknownVirus) {
                category = 3;
            } else if (hasNormalFever) {
                category = 4;
            }

            if (category == 0){
                continue;
            }

            patientsListMap.putIfAbsent(category, new ArrayList<>());
            patientsListMap.get(category).add(patient);
        }

        for (Map.Entry<Integer, List<Patient>> entry : patientsListMap.entrySet()) {
            System.out.println("-".repeat(50));
            System.out.println("CATEGORY " + entry.getKey());
            System.out.println("-".repeat(50));

            for (Patient patient1 : entry.getValue()) {
                System.out.println(patient1);
            }
        }

        System.out.println(queueOfPatients);
    }

    private static Collection<Patient> exercise2(Collection<Patient> uniquePatients) {
        Collection<Patient> patientQueue = uniquePatients.stream()
                .sorted(Comparator.comparing(Patient::isEnsured)
                        .thenComparing(Patient::getTemperature)
                        .thenComparing(Patient::getAge)
                        .reversed())
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));

//        for (Patient patient : patientQueue) {
//            System.out.println(patient);
//        }

        return patientQueue;
    }

    private static Collection<Patient> exercise1() {
        List<Patient> patients = Patient.getAllPatients();
        Collection<Patient> uniquePatients = new LinkedHashSet<>(patients);

//        for (Patient uniquePatient : uniquePatients) {
//            System.out.print(uniquePatient + " - ");
//        }
//
//        System.out.println();
//
//        for (Patient patient : patients) {
//            System.out.print(patient + " - ");
//        }

        return uniquePatients;
    }
}
