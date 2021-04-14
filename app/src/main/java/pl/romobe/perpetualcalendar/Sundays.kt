package pl.romobe.perpetualcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_sundays.*
import java.time.LocalDate
import java.time.LocalTime

class Sundays : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sundays)
        val extras = intent.extras ?: return
        var year:Int = extras.getInt("year");

        var easterDate: LocalDate
        try{
            easterDate = extras.getSerializable("easter") as LocalDate;
        }
        catch(e:ClassCastException){
            sundayHeader.text = "Błąd danych wejściowych - daty wielkanocy";
            return
        }
        if(year<=0){
            sundayHeader.text = "Błąd danych wejściowych - roku";
            return
        }
        if(year<2020){
            sundayHeader.text = "Niedziele handlowe w $year roku nie mają sensu";
            return
        }else{
            sundayHeader.text = "Niedziele handlowe w $year roku";
        }

        val list: MutableList<String> = mutableListOf();

        //statnie nd w sty,kw,czer,sierp
        var months = arrayOf(1, 4, 6, 8)
        for (index in months) {
            val entry = findSunday(LocalDate.of(year, index + 1, 1)).toString()
            list.add(entry);
        }
        //wielkanoc
        list.add(findSunday(easterDate).toString());
        //boze narodzenie
        list.add(findSunday(LocalDate.of(year, 12, 17)).toString());
        list.add(findSunday(LocalDate.of(year, 12, 24)).toString());

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list.sorted())
        sundayList.adapter = adapter;
    }

    fun back(v: View) {
        finish();
    }

    fun findSunday(date: LocalDate): LocalDate {
        var last = date.minusDays(1)
        while (last.dayOfWeek.value != 7) {
            last = last.minusDays(1);
        }
        return last
    }
}