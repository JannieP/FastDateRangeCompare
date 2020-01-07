package au.edu.qld.qcaa.sate.iamservice.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.sort;

public class DateRangeCompare {
    private static class DateRangeBase implements Comparable<DateRangeBase> {
        public final Date date;
        public final boolean isStart;
        public final DateRange dr;
        public DateRangeBase(Date date, boolean isStart, DateRange dr) {
            this.date = date;
            this.isStart = isStart;
            this.dr = dr;
        }

        @Override
        public int compareTo(DateRangeBase dr) {
            int dateCompare = date.compareTo(dr.date);
            if (dateCompare != 0) {
                return dateCompare;
            } else {
                if (!isStart && dr.isStart) {
                    return -1;
                } else if (isStart && !dr.isStart) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static boolean hasOverlappingDates(List<? extends DateRange> dateRangeList) {
        List<DateRangeBase> list = new ArrayList<>();
        dateRangeList.forEach(o -> {
            list.add(new DateRangeBase(o.getStartDate(), true, o));
            list.add(new DateRangeBase(o.getEndDate(), false, o));
        });

        sort(list);

        boolean overlap = false;

        HashSet<DateRange> active = new HashSet<>();
        for (DateRangeBase base : list) {
            if (!base.isStart) {
                active.remove(base.dr);
            } else {
                if (active.size() > 0) {
                    overlap = true;
                }
                active.add(base.dr);
            }
        }

        return overlap;
    }
}
