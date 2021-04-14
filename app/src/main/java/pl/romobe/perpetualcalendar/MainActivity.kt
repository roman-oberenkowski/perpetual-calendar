package pl.romobe.perpetualcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*;
import java.time.LocalDate
import java.util.logging.Logger
import kotlin.math.floor
import android.util.Log
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        holidayNames.text = "Popielec: \nWielkanoc: \nBoże Ciało: \nAdwent: ";
        yearPicker.maxValue = 2200;
        yearPicker.minValue = 1900;
        yearPicker.wrapSelectorWheel = false;
        yearPicker.value = 2021;
        yearPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            run {
                setDates(newVal);
            }
        }
        setDates(yearPicker.value)
    }
    fun setDates(year:Int){
        var easterDate =  getEasterDate(year)
        Log.i("WTF",easterDate.toString())
        var popielecDate= easterDate.minusDays(46);
        var bozecialoDate = easterDate.plusDays(60);
        var adwentDate = LocalDate.of(year,12,24)
        while(adwentDate.dayOfWeek.value!=7){
            adwentDate=adwentDate.minusDays(1);
        }
        adwentDate=adwentDate.minusDays(21);
        holidayDates.text = popielecDate.toString()+'\n'+easterDate.toString()+'\n'+bozecialoDate.toString()+'\n'+adwentDate.toString();
    }

    fun getEasterDate(year: Int): LocalDate {
        var a: Int = year % 19;
        var b: Int = floor(year / 100.0).toInt();
        var c: Int = year % 100;
        var d: Int = floor(b / 4.0).toInt();
        var e: Int = b % 4;
        var f: Int = floor((b + 8) / 25.0).toInt();
        var g: Int = floor((b - f + 1) / 3.0).toInt();
        var h: Int = (19 * a + b - d - g + 15) % 30;
        var i: Int = floor(c / 4.0).toInt();
        var k: Int = c % 4;
        var l: Int = (32 + 2 * e + 2 * i - h - k) % 7;
        var m: Int = floor((a + 11 * h + 22 * l) / 451.0).toInt();
        var p: Int = (h + l - 7 * m + 114) % 31
        var day: Int = p + 1;
        var month: Int = floor((h + l - 7 * m + 114) / 31.0).toInt();

        return LocalDate.of(year,month,day)
    }
    fun showSundays(v: View){
        val i= Intent(this,Sundays::class.java)
        i.putExtra("year",yearPicker.value)
        i.putExtra("easter",getEasterDate(yearPicker.value))
        startActivity(i);
    }
    fun showWorkdays(v: View){
        val i= Intent(this,workdays::class.java)
        startActivity(i);
    }

}