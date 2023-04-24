/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  2 апр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.util;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelUtils {


    public static LocalDate stringToDate(String s) {
        Pattern pattern = Pattern.compile("([1-9]|1[0-2])" +
                "(/)" +
                "((19[5-9]\\d)|(20[0-4])\\d)");
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()){

            int mount = Integer.parseInt(matcher.group(1));
            int your = Integer.parseInt(matcher.group(3));
            return LocalDate.of(your, mount, 1);
        }else{
            System.out.println("Формат даты задан в некорректном виде. Введите дату в формате \"MM/YYY\"");
            System.out.println("Вы ввели: " + s);
            return null;
        }
//
    }

    public static String dateToString(LocalDate ld){
        return ld.format(DateTimeFormatter.ofPattern("M/Y"));

    }
//        public static LocalDate of(int year, Month month) {
//            return LocalDate.of(year,month,1);
//
//        }
    }


