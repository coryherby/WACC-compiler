numberOfArgs = ARGV.length
inputformat = "Format should be of type: ./compile <filepath>"

if numberOfArgs == 0 then
    puts "No file path has been given!"
    puts inputformat
elsif numberOfArgs > 1
    puts "Too many arguments were given!"
    puts inputformat
else 
    file = ARGV[0]

armToTest = "tests/testARM/" + file.to_s

puts
command = "arm-linux-gnueabi-gcc -o " + armToTest + " -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + armToTest + ".s"
puts command
system command

puts
command = "qemu-arm -L /usr/arm-linux-gnueabi/ " + armToTest
puts command
system command
exitCode = $?.exitstatus

puts
puts "return exit code : " + exitCode.to_s
puts

command = "rm " + armToTest
system command
end
