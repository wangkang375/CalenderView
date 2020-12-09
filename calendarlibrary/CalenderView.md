# CalenderView
**初型  目前支持单选**

可自行增加多选，标签，主要逻辑在monthView  ，自定义View

也可参考官方 SimpleMonthView

**大体思路**

1. 月份视图（monthView）  自定义View,
2. 年视图（yearView）RecycleView  item为month   
3. 月份吸顶头自定义ItemDecoration 
4. calendarView 为 viewpage2  item为yearView  