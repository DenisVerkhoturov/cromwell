task increment {
  Int i
  command {
    echo $(( ${i} + 1 ))
  }
  output {
    Int j = read_int(stdout())
  }
  runtime {
    docker: "us.gcr.io/google-containers/ubuntu-slim:0.14"
  }
}

workflow subwf {
  Array[Int] is
  scatter (i in is) {
    call increment { input: i = i }
  }
  output {
    Array[Int] js = increment.j
  }
}
