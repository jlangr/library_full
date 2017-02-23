package util;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static util.matchers.LessThan.lessThan;

public class AnAuto {
    @Test
    public void idlesEngineWhenStarted() {

        Auto auto = new Auto();
        auto.DepressBrake();

        auto.PressStartButton();

        assertThat(auto.RPM(), is(both(greaterThan(950)).and(lessThan(1100))));
    }

    class Auto {

        public void DepressBrake() {
        }

        public void PressStartButton() {
        }

        public int RPM() {
            return 1000;
        }
    }
}
