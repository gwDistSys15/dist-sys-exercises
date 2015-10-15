# MPI Tutorial
MPI is a library to facilitate writing parallel programs.

## Installation Instructions
Follow these steps to install MPI on a Koding.com VM
```
# Download the mpi source code:
        wget http://www.mpich.org/static/downloads/3.1.4/mpich-3.1.4.tar.gz
# Extract the code:
        tar xzf mpich-3.1.4.tar.gz
# Install dependencies:
        sudo apt-get install build-essential
# Configure and install the library
        cd mpich-3.1.4
        ./configure --disable-fortran
        make && sudo make install
# Test that MPI is installed:
        mpiexec --version
```

Running the last command should give output like:
```
twood02: ~/mpich-3.1.4 $ mpiexec --version
HYDRA build details:
    Version:                                 3.1.4
    Release Date:                            Fri Feb 20 15:02:56 CST 2015
    ...
```

## Hello MPI
Here is the code for "hello world" in MPI:

```
#include <mpi.h>
#include <stdio.h>

int main(int argc, char** argv) {
    // Initialize the MPI environment
    MPI_Init(NULL, NULL);

    // Get the number of processes
    int world_size;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Get the rank of the process
    int world_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    // Get the name of the processor
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    int name_len;
    MPI_Get_processor_name(processor_name, &name_len);

    // Print off a hello world message
    printf("Hello world from processor %s, rank %d"
           " out of %d processors\n",
           processor_name, world_rank, world_size);

    // Finalize the MPI environment.
    MPI_Finalize();
}
```

Put this code into a file named ``hello.c``.  Then you can compile and run it with:
```
# compile with MPI compiler
mpicc hello.c -o hello
# run with eight processes:
mpirun -n 8 ./hello
```

**Remember:** you can't just run the program with `./hello` like you normally would, you need to use the `mpirun` command if you want it to work with multiple processes.

## Sending and Receiving
The API for sending and receiving is:
```
MPI_Send(
    void* data,
    int count,
    MPI_Datatype datatype,
    int destination,
    int tag,
    MPI_Comm communicator)

MPI_Recv(
    void* data,
    int count,
    MPI_Datatype datatype,
    int source,
    int tag,
    MPI_Comm communicator,
    MPI_Status* status)
```
For example, a simple program that causes the Rank 0 node to send a packet to the Rank 1 node might have code like this:
```
int rank;
int number;

// MPI init code would be here...

MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if(rank == 0) {
        number = 123;
        MPI_Send(&number, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
} else if (world_rank == 1) {
        MPI_Recv(&number, 1, MPI_INT, 0, 0, MPI_COMM_WORLD,
        MPI_STATUS_IGNORE);
}
```
This uses a tag of 0, the default communicator, and does not generate a status struct on the receive path.

#### Loop Counter Exercise:

Write a program designed for sending a message around a "ring" of N processes.  The process with rank 0 will start by sending a the number ``1`` to the process with rank 1, which increment the number and then send it to the process with rank 2, etc.  Once the
message reaches process N-1, it should send it to process 0.  Process 0 should then print out the integer (which should be the count of how many processes there are).  Each process should print out a message when it receives the counter.

#### MPI Tests
Write small programs to test the behavior in these different cases:

  - What happens if a Node tries to call receive on a source that isn't trying to send to it?
  - What happens if Node 0 sends a float and then an int to Node 1, but Node 1 tries to first receive an int and then receive the float?
  - What happens if Node 0 sends an int with tag 1 and Node 2 tries to receive an int with tag 5?
  - In the example above, what happens if Node 2 tries to receive an int with tag `MPI_ANY_TAG`?

These will take a little more research on your part to figure out:
  - What is the ``MPI_Barrier`` function for? Write a small program that demonstrates its use.
  - Is it possible to broadcast a message from node 0 to all others in one function call? Write a program to do this?
