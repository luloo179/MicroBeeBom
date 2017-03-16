package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.joystick;

import android.view.InputDevice;
import android.view.MotionEvent;

/**
 * Created by jangsangjin on 2017. 2. 17..
 */

public class JoystickProcess {

    float x, y, z, rz;

    public float getX() {

        return this.x;

    }

    public float getY() {

        return this.y;

    }

    public float getZ(){

        return this.z;

    }

    public float getRz(){

        return this.rz;

    }

    public void processJoystickInput(MotionEvent event,
                                     int historyPos) {

        InputDevice mInputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        this.x = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_X, historyPos);
        if (this.x == 0) {

            this.x = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_X, historyPos);
        }

        this.z = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Z, historyPos);
        if (this.z == 0) {

            this.z = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_Z, historyPos);
        }

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        this.y = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Y, historyPos);
        if (this.y == 0) {

            this.y = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_Y, historyPos);

        }

        this.rz = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_RZ, historyPos);
        if (this.rz == 0) {

            this.rz = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_RZ, historyPos);

        }

        // Update the ship object based on the new x and y values
    }

    private float getCenteredAxis(MotionEvent event,
                                  InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis) :
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

}
