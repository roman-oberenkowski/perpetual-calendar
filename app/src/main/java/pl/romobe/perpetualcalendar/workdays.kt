package pl.romobe.perpetualcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_main.*;
import kotlinx.android.synthetic.main.activity_sundays.*
import kotlinx.android.synthetic.main.activity_workdays.*
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import kotlin.math.floor

class workdays : AppCompatActivity() {
    var easterDates:MutableMap<Int,LocalDate> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workdays)
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(1902, Calendar.JANUARY, 1)
        startDatePicker.minDate = calendar.time.time
        endDatePicker.minDate = calendar.time.time
        startDatePicker.setOnDateChangedListener{ datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            calculateDays()
        }

        endDatePicker.setOnDateChangedListener{ datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            calculateDays()
        }
        setWorkdaysOutput(0,0)
    }
    fun back(v: View) {
        finish();
    }
    fun calculateDays(){
        var startD=LocalDate.of(startDatePicker.year,startDatePicker.month+1,startDatePicker.dayOfMonth)
        var endD=LocalDate.of(endDatePicker.year,endDatePicker.month+1,endDatePicker.dayOfMonth)
        if(startD.isAfter(endD)){
            workdaysOutput.text="Błędny zakres"
        }else{
            if(endD.year-startD.year>2){
                workdaysOutput.text="Zbyt duży okres"
            }else{
                var daysCounter=0
                var workdaysCounter=0

                while(endD.isAfter(startD)||endD.equals(startD)){
                    Log.i("w2",daysCounter.toString())
                    daysCounter++
                    if(!checkIfHoliday(startD)){
                        workdaysCounter++
                    }
                    startD=startD.plusDays(1);
                }
                setWorkdaysOutput(daysCounter,workdaysCounter)
            }
        }
    }
    fun setWorkdaysOutput(days:Int,workdays:Int){
        workdaysOutput.text="Dni: $days\n Dni roboczych: $workdays";
    }
    fun checkIfHoliday(date:LocalDate):Boolean{
        if(date.dayOfWeek.value == 6 || date.dayOfWeek.value == 7)return true
        val months= arrayOf<Int>(1,1,5,5,8,11,11,12,12)
        val days=arrayOf<Int>(1,6,1,3,15,1,11,25,26)
        for(i in 0..months.size-1){
            if(date.equals(LocalDate.of(date.year,months[i],days[i])))return true
        }

        if(easterDates.get(date.year)==null){
            easterDates.put(date.year,getEasterDate(date.year))
        }
        if(date.equals(easterDates[date.year]!!.plusDays(1)))return true
        return false
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
}