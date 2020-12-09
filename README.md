
# CalenderView
**初型  目前支持单选**

可自行增加多选，标签，主要逻辑在monthView  ，自定义View

也可参考官方 SimpleMonthView

**大体思路**

1. 月份视图（monthView）  自定义View,
2. 年视图（yearView）RecycleView  item为month   
3. 月份吸顶头自定义ItemDecoration 
4. calendarView 为 viewpage2  item为yearView  

**效果**

![calendar](C:\Users\Administrator\Desktop\calendar.jpg)



```kotlin
class MainActivity : AppCompatActivity(), SelectDayListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<CalendarView>(R.id.cnv).setYearRange(2002..2020, this)
    }

    override fun onSelectDayListener(dayCBean: DayCBean) {
        Toast.makeText(
            this,
            "${dayCBean.year}年${dayCBean.month + 1}月${dayCBean.day}日",
            Toast.LENGTH_SHORT
        ).show()
    }
}
```

