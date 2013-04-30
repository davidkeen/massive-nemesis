sed 's/$/ /' input.txt | tr ' \n' ', ' | sed 's/, $//' > output.txt
