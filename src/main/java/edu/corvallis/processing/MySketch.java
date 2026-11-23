package edu.corvallis.processing;

public class MySketch extends ProcessingSketch {
    String state = "green";
    float carX = 40;
    int[] redLightPos = {510, 55};
    int[] yellowLightPos = {510, 115};
    int[] greenLightPos = {510, 175};
    double maxSpeed = 10;
    double currentSpeed = maxSpeed;
    double acceleration = 0.3;
    double deceleration = 0.1;
    double minSpeed = 2;
    int currentFrame = 0;
    double second = 0;
    int redDef = 185;
    int greenDef = 250;
    int blueDef = 250;
    int[] rgbDay = {redDef, greenDef, blueDef};
    int[] rgbNight = {20, 20, 60};
    int[] currentRGB = rgbDay.clone();
    String DayCycle = "Day";

    @Override
    public void settings() {
        size(600, 400);
    }

    @Override
    public void draw() {
        background(currentRGB[0], currentRGB[1], currentRGB[2]);
        drawRoad();
        drawLight();
        drawCar(carX, 305);
        moveCar();
        currentFrame++;
        second = currentFrame / 60.0;
        fill(255, 0, 0);
        text("Second: " + (int) second, 10, 20);

        // Switch the light once every 5 seconds (300 frames), not on frame 0
        if (currentFrame % 300 == 0) {
            switch (state) {
                case "green" -> state = "yellow";
                case "yellow" -> state = "red";
                case "red" -> state = "green";
            }
        }
       
        int cycleFrame = currentFrame % 2400;   
        float t;                               
        if (cycleFrame < 600) {                 
            t = cycleFrame / 600.0f;
            DayCycle = "Night";
        } else if (cycleFrame < 1200) {         
            t = 1;
        } else if (cycleFrame < 1800) {         
            t = 1 - (cycleFrame - 1200) / 600.0f;
            DayCycle = "Day";
        } else {                                
            t = 0;
        }
        for (int i = 0; i < 3; i++) {
            currentRGB[i] = Math.round(rgbDay[i] + (rgbNight[i] - rgbDay[i]) * t);
        }
      
    }

    public void drawRoad() {
        noStroke();
        fill(70);
        rect(0, 300, 600, 100);
        fill(255, 210, 0);
        for (int x = 0; x < 600; x += 60) {
            rect(x, 357, 30, 6);
        }
    }

    public void drawLight() {
        stroke(0);
        fill(60);
        rect(505, 210, 10, 90);       // pole
        rect(475, 20, 70, 190, 10);   // box

        // red
        if (state.equals("red")) {
            fill(230, 40, 40);
        } else {
            fill(90, 30, 30);
        }
        ellipse(redLightPos[0], redLightPos[1], 50, 50);

        // yellow
        if (state.equals("yellow")) {
            fill(250, 210, 40);
        } else {
            fill(90, 80, 20);
        }
        ellipse(yellowLightPos[0], yellowLightPos[1], 50, 50);

        // green
        if (state.equals("green")) {
            fill(60, 200, 80);
        } else {
            fill(25, 75, 35);
        }
        ellipse(greenLightPos[0], greenLightPos[1], 50, 50);
    }

    public void drawCar(float x, float y) {
        stroke(0);
        fill(30, 90, 200);
        rect(x + 25, y - 25, 65, 30, 8);   // roof
        rect(x, y, 120, 35, 8);            // body
        fill(20);
        ellipse(x + 25, y + 35, 28, 28);   // back wheel
        ellipse(x + 95, y + 35, 28, 28);   // front wheel
    }

    public void moveCar() {
        carX += currentSpeed;
        switch (state) {
            case "green" -> {
                currentSpeed += acceleration;
                if (currentSpeed > maxSpeed) {
                    currentSpeed = maxSpeed;
                }
            }
            case "yellow" -> {
                currentSpeed -= deceleration;
                if (currentSpeed < minSpeed) {
                    currentSpeed = minSpeed;
                }
            }
            case "red" -> {
                currentSpeed -= deceleration;
                if (currentSpeed < 0) {
                    currentSpeed = 0;
                }
            }
            default -> {
            }
        }
        if (carX > width) {
            carX = -120;
        }
    }

    @Override
    public void mouseMoved() {
    }

    @Override
    public void mousePressed() {
        double dRed = calculateDistance(mouseX, mouseY, redLightPos[0], redLightPos[1]);
        double dYellow = calculateDistance(mouseX, mouseY, yellowLightPos[0], yellowLightPos[1]);
        double dGreen = calculateDistance(mouseX, mouseY, greenLightPos[0], greenLightPos[1]);

        if (dRed <= 25) {
            state = "red";
        } else if (dYellow <= 25) {
            state = "yellow";
        } else if (dGreen <= 25) {
            state = "green";
        }
    }

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Main method
    public static void main(String[] args) {
        launch(MySketch.class);
    }
}