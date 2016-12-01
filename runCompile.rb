numberOfArgs = ARGV.length
inputformat = "Format should be of type: ./compile <filepath>"

if numberOfArgs == 0 then
    puts "No file path has been given!"
    puts inputformat
elsif numberOfArgs > 1
    puts "Too many arguments were given!"
    puts inputformat
else 
    file_path = ARGV[0]
    new_generated_file_path = File.basename(file_path, ".*") + ".s"
    new_generated_file_path = File.basename(file_path, ".*") + ".s"

    system "make clean"
    system "make"

    commandCompile = "ruby compile " + file_path
    system commandCompile

    puts "\n---------- COMMANDS GENERATED ----------\n"

    commandPrintAndDeleteFile = "cat -n " + new_generated_file_path + " && " \
        + "rm " + new_generated_file_path
    system commandPrintAndDeleteFile

    puts "\n----------------------------------------\n"
end
