package ScheduleCreator.models;

/**
 * This class stores various course information.
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/27/2020
 */

import ScheduleCreator.Translator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Course {

    protected final String fullText;
    protected final String id;
    protected final String abbreviation;
    protected final String courseNumber;
    protected List<Section> sections;

    public Course(String _name, String _semester) {
        this.fullText = _name;
        this.courseNumber = _name.substring(4, 7);
        this.abbreviation = _name.substring(0, 4);
        this.id = this.abbreviation + this.courseNumber;
        this.loadSectionsFromFile( _semester);
    }

    //=================  GETTERS ===============

    public String getID() {
        return this.id;
    }

    public String getFullText() {
        return this.fullText;
    }

    public String getabbreviation() {
        return abbreviation;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public List<Section> getSections() {
        return this.sections;
    }

    private void loadSectionsFromFile(String _semester) {
        List<String> sectionStrings = Translator.getSections(this.id, _semester);
        this.sections = new ArrayList();
        Pattern p = Pattern.compile(".*([0-9]{5}).*- ([0-9]{2})\\s*(\\S* [ap]m - \\S* [ap]m)\\s*(\\S*)(.*)=(.*)");

        //This pattern is to be matched with Courses with "TBA" as location and meeting times"
        Pattern p2 = Pattern.compile(".*([0-9]{5}).*- ([0-9]{2})\\s*(TBA\\s*TBA )=(.*)");
        Matcher m, m2;

        String CRN = "";
        String sectionNumber = "";
        String daysAndTimes = "";
        String location = "";
        String instructor = "";

        for (String sectionString : sectionStrings) {
            m = p.matcher(sectionString);
            m2 = p2.matcher(sectionString);
            if ((m.matches())) {

                CRN = m.group(1).trim();
                sectionNumber = m.group(2).trim();
                daysAndTimes = m.group(4).trim() + " " + m.group(3).trim();
                location = m.group(5).trim();
                instructor = m.group(6).trim();
                Section newSection = new Section(this.id, sectionNumber, daysAndTimes, location, instructor, CRN, false);

                this.sections.add(newSection);
            }
            else if ((m2.matches())) {
                CRN = m2.group(1).trim();
                sectionNumber = m2.group(2).trim();
                instructor = m2.group(4).trim();
                Section newSection = new Section(this.id, sectionNumber, daysAndTimes, location, instructor, CRN, true);
                this.sections.add(newSection);
            }
        }
    }
}