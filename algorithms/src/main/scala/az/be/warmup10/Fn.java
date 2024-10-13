package lesson56s6.warmup;

interface Fn {

  static int random_val(int min, int max) {
    return (int)((Math.random() * (max - min + 1)) + min);
  }

}
