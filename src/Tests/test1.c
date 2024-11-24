int main() {
 int x = 2;
float y = 3;
 switch (x) {
 case 1: {
 y = 10;
 break;
}
 case 2: {
 y = 20.3;
 func(y);
 break;
}
 default: {
 return 0;
}
 }
}