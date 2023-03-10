ȘUIU ANA-MARIA 332CC
Tema 2 APD

In clasa Tema2:
Avem un executor cu maxim P thread-uri de care ne folosim pentru a incepe sa cream
thread-urile care se ocupa de comenzi/orders. Astfel, dam submit creand astfel o
instanta de tip MyRunnable.
In clasa MyRunnable:
Aceasta apeland singura functia run, incepe sa citeasca cu ajutorul unei variabile
BufferedReader(reader) din fisierul orders. Citeste prima linie, iar apoi intr-un
while urmeaza sa citeasca comenzile urmatoare. In while, facem split la linia curenta
in partea cu comanda si partea cu numarul produselor. Ne folosim de un
AtomicInteger(inQueue) pentru a monitoriza numarul de taskuri care se creeaza.
Dam submit pe acelasi executor, pentru a crea un alt task, avand acelasi reader trimis
ca parametru, pentru a putea continua citirea de la urmatoarea linie din fisier. Daca
numarul de produse al comenzi nu e 0, cream un obiect de tip Order si apelam metoda
submit. Citim o noua linie din fisier si se relueaza while-ul. La final, decrementam
variabila inQueue pentru a scadea numarul de taskuri. Daca aceasta a ajuns la 0
inseamna ca task-urile au ajuns la final si thread-urile si-au terminat treaba.
In acest caz, cu ajutorul unui alt reader de tip BufferedReader citim fiecare linie
din orders.txt si daca in linia/comanda curenta nu avem 0 produse, scriem cu ajutorul
unui writer de tip BufferedWriter in fisierul de orders_out linia + textul",shipped".
Dupa ce am terminat inchidem cele 2 readere, writerul si executorul.
In clasa Order:
Prin metoda "submit", dam submit la un task pentru angajatorii care se ocupa de produse.
Ne folosim de un executor care are maxim P thread-uri care va fi acelasi pentru toti
angajatorii(threadurile) care se ocupa de produse. De asemenea avem un AtomicInteger
unde vom retine numarul de task-uri.
Clasa Thread:
In metoda run citim cu ajutorul unui reader din fisierul "order_products.txt". In while,
facem split la linia curenta in partea cu comanda si partea cu numele produsului.
Verificam daca comanda din linie este aceeasi cu comanda transmisa ca parametru de care
trebuie sa se ocupe thread-urile acestei comenzi. Daca este aceeasi, cu ajutorul unui
writer scriem in order_products_out.txt linia + ",shipped". Aceasta verificare e pusa
intr-un lock pentru a nu putea scrie 2 threaduri in acelasi timp in fisierul out.
Dam submit unui nou task cu acelasi executor pentru produse, avand ca parametrii
aceiasi reader si writer folositi in instanta. Trecem la linia urmatoare si in while
cu ajutorul reader-ului. La final decrementam variabila inQueue pentru a vedea cand
ajunge la 0, adica cand s-au terminat de efectuat toate taskurile. Cand aceasta e 0,
inchidem writerul si readerul folositi si executorul.
